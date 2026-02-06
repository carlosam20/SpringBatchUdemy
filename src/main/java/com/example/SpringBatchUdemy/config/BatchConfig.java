package com.example.SpringBatchUdemy.config;

import com.example.SpringBatchUdemy.dto.XMLSensorDataStructure;
import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Configuration
public class BatchConfig extends DefaultBatchConfiguration{

    Log log = LogFactory.getLog(BatchConfig.class);

    private final Environment environment;

    @Value("classpath:input/HTE2NP.txt")
    Resource resourceTxT;

    WritableResource resourceXML = new FileSystemResource("output/data.xml");





    public BatchConfig(Environment environment) {
        this.environment = environment;
    }










    @Qualifier("convertSensorDataXML")
    @Bean
    public Job sensorData(
            JobRepository jobRepository,
            Step aggregateSensorData) throws Exception {
        log.info("Starting sensor data job");
        return new JobBuilder("processSensorData", jobRepository)
                .start(aggregateSensorData)
                .build();
    }

    @Bean
    public DataSource dataSource(){

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("spring.datasource.driver-class-name")));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        return dataSource;
    }


    @Bean
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();

        // Wire the dependencies
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);

        // Critical for Postgres: explicitly set the type to avoid "guessing" errors
        factory.setDatabaseType("POSTGRES");

        // Optional: Ensure the factory initializes correctly
        factory.afterPropertiesSet();

        return factory.getObject();
    }





    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }





    @Bean
    public FlatFileItemReader<InputSensorDataDTO> itemReader() throws Exception{
        MultiSplitterTokenizer splitterTokenizer = new MultiSplitterTokenizer();
        splitterTokenizer.setNames("date","temps");
        log.info("Starting Reader txt");
        return new FlatFileItemReaderBuilder<InputSensorDataDTO>()
                .name("tempItemReader")
                .resource(resourceTxT)
                .lineTokenizer(splitterTokenizer)
                .fieldSetMapper(new RecordFieldSetMapper<>(InputSensorDataDTO.class))
                .build();
    }


    @Bean
    public XStreamMarshaller tempMarshaller() {
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String, Class<?>> aliases = new HashMap<>();
        // Assuming your XML looks like: <weather><date>...</date></weather>
        aliases.put("daily-data", XMLSensorDataStructure.class);
        marshaller.setAliases(aliases);
        marshaller.setSupportedClasses(XMLSensorDataStructure.class);
        return marshaller;
    }

    @Bean
    public StaxEventItemWriter<XMLSensorDataStructure> sensorDataDTOStaxEventItemWriter() {

        log.info("Starting Writer XML");
        return new StaxEventItemWriterBuilder<XMLSensorDataStructure>()
                .name("tempItemWriter")
                .resource(resourceXML)
                .marshaller(tempMarshaller())
                .rootTagName("data")
                .overwriteOutput(true)
                .encoding("UTF-8")
                .build();
    }



    @Bean
    public Step aggregateSensorData(
            PlatformTransactionManager platformTransactionManager,
            SensorDataProcessor sensorDataProcessor,
            FlatFileItemReader<InputSensorDataDTO> itemReader,
            StaxEventItemWriter<XMLSensorDataStructure> sensorDataDTOStaxEventItemWriter) throws Exception {
            log.info("Starting aggregateSensorData");
        return new StepBuilder("process-sensor-data",jobRepository())
                .<InputSensorDataDTO,XMLSensorDataStructure>chunk(10, platformTransactionManager)
                .chunk(10).transactionManager(platformTransactionManager)
                .reader(itemReader)
                .processor(sensorDataProcessor)
                .writer(sensorDataDTOStaxEventItemWriter)
                .build();
    }




//    @Bean
//    public Step moveAnomalies(PlatformTransactionManager platformTransactionManager) throws Exception {
//        log.info("Starting move anomalies step txt");
//        return new StepBuilder("process-move-not-correct-data",jobRepository())
//                .<InputSensorDataDTO,OutputXMLSensorDataDTO>chunk(10, platformTransactionManager)
//                .chunk(10).transactionManager(platformTransactionManager)
//                .reader(itemReader())
//                .processor()
//                .writer()
//                .build();
//    }
}

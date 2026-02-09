package com.example.SpringBatchUdemy.config;

import com.example.SpringBatchUdemy.dto.XMLSensorDataStructure;
import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import com.example.SpringBatchUdemy.mapper.SensorDataFieldSetMapper;
import com.example.SpringBatchUdemy.listener.ProcessDebugListener;
import com.example.SpringBatchUdemy.listener.ReadDebugListener;
import com.thoughtworks.xstream.security.ExplicitTypePermission;
import lombok.NonNull;
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
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Configuration
public class BatchConfig extends DefaultBatchConfiguration{

    Log log = LogFactory.getLog(BatchConfig.class);

    @Autowired
    Environment environment;

    @Value("classpath:input/HTE2NP.txt")
    Resource resourceTxT;

    WritableResource resourceXML = new FileSystemResource("src/main/resources/output/data.xml");


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
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("spring.datasource.driver-class-name")));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        return dataSource;
    }

    // --- 2. TRANSACTION MANAGER DEFINITION ---
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

    @Override
    protected DataSource getDataSource() {
        return dataSource();
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return transactionManager(dataSource());
    }

    @Bean
    @Override
    @NonNull
    public  JobRepository jobRepository() {
        try {
            JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
            factory.setDataSource(getDataSource());
            factory.setTransactionManager(getTransactionManager());

            // Explicitly set the type to POSTGRES to ensure correct SQL dialects.
            factory.setDatabaseType("POSTGRES");

            // CRITICAL FOR POSTGRESQL:
            // Spring Batch defaults to ISOLATION_SERIALIZABLE.
            // ISOLATION_READ_COMMITTED prevents serialization errors in PostgreSQL.
            factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");

            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize JobRepository", e);
        }
    }

    @Bean
    public FlatFileItemReader<InputSensorDataDTO> itemReader() {
        MultiSplitterTokenizer splitterTokenizer = new MultiSplitterTokenizer();
        splitterTokenizer.setNames("date", "temps");
        log.info("Starting Reader txt");
        return new FlatFileItemReaderBuilder<InputSensorDataDTO>()
                .name("tempItemReader")
                .strict(true)
                .resource(resourceTxT)
                .lineTokenizer(splitterTokenizer)
                .fieldSetMapper(new SensorDataFieldSetMapper())
                .build();
    }


    @Bean
    public XStreamMarshaller tempMarshaller() {
        try{
            XStreamMarshaller marshaller = new XStreamMarshaller();
            Map<String, Class<?>> aliases = new HashMap<>();
            aliases.put("daily-data", XMLSensorDataStructure.class);
            marshaller.setAnnotatedClasses(XMLSensorDataStructure.class);
            ExplicitTypePermission typePermission = new ExplicitTypePermission(new Class[]
                    {
                        XMLSensorDataStructure.class
                    });
            marshaller.setAliases(aliases);
            marshaller.setTypePermissions(typePermission);
            marshaller.setSupportedClasses(XMLSensorDataStructure.class);
            return marshaller;
        } catch (XmlMappingException e) {
            throw new RuntimeException(e);
        }

    }

    @Bean
    public StaxEventItemWriter<XMLSensorDataStructure> sensorDataDTOStaxEventItemWriter() {
        log.info("Starting Writer XML");
        return new StaxEventItemWriterBuilder<XMLSensorDataStructure>()
                .name("tempItemWriter")
                .marshaller(tempMarshaller())
                .resource(resourceXML)
                .rootTagName("data")
                .encoding("UTF-8")
                .standalone(true)
                .overwriteOutput(true)
                .build();
    }


    @Bean
    public Step aggregateSensorData(
            PlatformTransactionManager platformTransactionManager,
            SensorDataProcessor sensorDataProcessor,
            FlatFileItemReader<InputSensorDataDTO> itemReader,
            StaxEventItemWriter<XMLSensorDataStructure> sensorDataDTOStaxEventItemWriter) {
            log.info("Starting aggregateSensorData");
        return new StepBuilder("process-sensor-data",jobRepository())
                .<InputSensorDataDTO,XMLSensorDataStructure>chunk(10, platformTransactionManager)
//                .chunk(10).transactionManager(platformTransactionManager)
                .reader(itemReader)
                .listener(new ReadDebugListener())
                .processor(sensorDataProcessor)
                .listener(new ProcessDebugListener())
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

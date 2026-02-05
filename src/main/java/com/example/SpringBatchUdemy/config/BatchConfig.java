package com.example.SpringBatchUdemy.config;

import com.example.SpringBatchUdemy.dto.InputSensorDataDTO;
import com.example.SpringBatchUdemy.dto.OutputXMLSensorDataDTO;
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
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration{

    private final Environment environment;
    private final Resource resourceTxT = new FileSystemResource("input/HTE2NP.txt");
    private final WritableResource resourceXML = new FileSystemResource("output/format.xml");



    public BatchConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Job sensorData(JobRepository jobRepository) {
        return new JobBuilder("processSensorData", jobRepository)
                .start()
                .build();
    }

    @Bean
    public DataSource domainDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        return dataSource;
    }


    @Bean
    public JobRepository jobRepository() {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(domainDataSource());
        factory.setDatabaseType("db2");
        factory.setTransactionManager(batchTransactionManager(domainDataSource()));
        try {
            return factory.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Bean
    public JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }





    @Bean
    public FlatFileItemReader<InputSensorDataDTO> itemReader() throws Exception{
        MultiSplitterTokenizer splitterTokenizer = new MultiSplitterTokenizer();
        splitterTokenizer.setNames("date","minTemp","avgTemp","maxTemp");
        return new FlatFileItemReaderBuilder<InputSensorDataDTO>()
                .name("tempItemReader")
                .resource(resourceTxT)
                .lineTokenizer(splitterTokenizer)
                .fieldSetMapper(new RecordFieldSetMapper<>(InputSensorDataDTO.class)) // Your custom mapper for Records
                .build();
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(OutputXMLSensorDataDTO.class);
        return marshaller;
    }

    @Bean
    public StaxEventItemWriter<PulsarProperties.Transaction> sensorDataDTOStaxEventItemWriter(FlatFileItemReader<InputSensorDataDTO> inputSensorDataDTOFlatFileItemReader){
        return new StaxEventItemWriterBuilder<PulsarProperties.Transaction>()
                .name("tempItemWriter")
                .resource(resourceXML)
                .encoding("UTF-8")
                .build();
    }



    @Bean
    public Step sensorData(PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("process-sensor-data",jobRepository())
                .<InputSensorDataDTO,OutputXMLSensorDataDTO>chunk(10, platformTransactionManager)
                .chunk(10).transactionManager(platformTransactionManager)
                .reader(itemReader())
                .processor(sensorDataProcessor())
                .writer()
                .build();
    }




    @Bean
    public Step moveNotCorrectData(PlatformTransactionManager platformTransactionManager) throws Exception {
        return new StepBuilder("process-move-not-correct-data",jobRepository())
                .<InputSensorDataDTO,OutputXMLSensorDataDTO>chunk(10, platformTransactionManager)
                .chunk(10).transactionManager(platformTransactionManager)
                .reader(itemReader())
                .processor()
                .writer()
                .build();
    }
}

package com.example.SpringBatchUdemy.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {


//    Name,Age,Email,Address,Phone
//    @Bean
//    public FlatFileItemReader<ProcessorDTO> customerReader() {
//        return new FlatFileItemReaderBuilder<ProcessorDTO>()
//                .name("customerItemReader")
//                .resource(new ClassPathResource("users.csv"))
//                .delimited()
//                .names("name", "email", "address","phone")
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
//                    setTargetType(ProcessorDTO.class);
//                }})
//                .build();
//    }
//
//    @Bean
//    public Job job(JobRepository jobRepository) {
//        return new JobBuilder("job", jobRepository)
//                .start(splitFlow())
//                .next(step4())
//                .build()        //builds FlowJobBuilder instance
//                .build();       //builds Job instance
//    }
//
//    @Bean
//    public Flow splitFlow() {
//        return new FlowBuilder<SimpleFlow>("splitFlow")
//                .split(taskExecutor())
//                .add(flow1(), flow2())
//                .build();
//    }
//
//    @Bean
//    public Flow flow1() {
//        return new FlowBuilder<SimpleFlow>("flow1")
//                .start(step1())
//                .next(step2())
//                .build();
//    }
//
//    @Bean
//    public Flow flow2() {
//        return new FlowBuilder<SimpleFlow>("flow2")
//                .start(step3())
//                .build();
//    }
//
//    @Bean
//    public TaskExecutor taskExecutor() {
//        return new SimpleAsyncTaskExecutor("spring_batch");
//    }
}

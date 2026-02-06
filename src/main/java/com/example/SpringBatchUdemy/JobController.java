package com.example.SpringBatchUdemy;

import com.example.SpringBatchUdemy.config.BatchConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class JobController {

    Log log = LogFactory.getLog(BatchConfig.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    // Inject the specific job bean you defined in your config
    private Job sensorData;

    @GetMapping("/start")
    public ResponseEntity<String> startJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .toJobParameters();
            jobLauncher.run(sensorData, params);
            log.info("job controler finished");
            return ResponseEntity.ok("Job Started");
        } catch (Exception e) {
            log.error("Error on job controler");
            return ResponseEntity.ok("Job Failed");
        }
    }
}


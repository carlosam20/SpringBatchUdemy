package com.example.SpringBatchUdemy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class JobController {


        private final Log log = LogFactory.getLog(com.example.SpringBatchUdemy.JobController.class);

        @Autowired
        private JobLauncher jobLauncher;

        @Autowired
        private Job sensorData;

        @GetMapping("/start")
        public ResponseEntity<String> startJob() {

            try {
                JobParameters params = new JobParametersBuilder()
                        .addString("JobID", String.valueOf(System.currentTimeMillis()))
                        .toJobParameters();

                // The run method returns an object containing the result status
                JobExecution execution = jobLauncher.run(sensorData, params);

                if(execution.isRunning()){
                    log.info("Job status: running");
                } else if (execution.isStopping()) {
                    log.info("Job status: stopping");
                }


                if (execution.getStatus().isUnsuccessful() || execution.getStatus() == BatchStatus.FAILED) {
                    // Check for exceptions that happened during the step
                    execution.getAllFailureExceptions().forEach(throwable -> {
                        log.error("Internal Job Error: {}", throwable);
                    });
                    return ResponseEntity.status(500).body("Job Failed internally. Check logs.");
                }

                log.info("Job controller finished successfully");
                return ResponseEntity.ok("Job Finished with status: " + execution.getStatus());

            } catch (JobExecutionAlreadyRunningException | JobRestartException |
                     JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
                log.error("Job could not be started: {}", e);
                return ResponseEntity.badRequest().body("Job startup failed");
            } catch (Exception e) {
                log.error("Unexpected error on job controller", e);
                return ResponseEntity.internalServerError().body("System Error");
            }
        }
    }



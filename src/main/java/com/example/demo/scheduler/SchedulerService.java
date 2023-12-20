package com.example.demo.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;


@Service
public class SchedulerService {

    private final JobLauncher jobLauncher;

    private final Job job;


    public SchedulerService (JobLauncher jobLauncher, Job job){

        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    
}

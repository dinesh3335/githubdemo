package com.example.springbatchdemo.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.Date;

public class MyJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Started date and time :"+ new Date());
        System.out.println("status at starting :"+ jobExecution.getStatus());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("End date and time :"+ new Date());
        System.out.println("status at ending :"+ jobExecution.getStatus());
    }
}
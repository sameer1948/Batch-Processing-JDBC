package com.spring.batch.jdbc.listener;

import java.util.Date;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListener implements JobExecutionListener{

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("Started Date and Time :: " + new Date());
		System.out.println("Status  :: " + jobExecution.getStatus());
	}
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Ended Date and Time :: " + new Date());
		System.out.println("Status  :: " + jobExecution.getStatus());
	}
	
}

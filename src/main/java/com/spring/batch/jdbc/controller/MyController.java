package com.spring.batch.jdbc.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/jdbc")
public class MyController {
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@GetMapping("show")
	public String getData() {
		JobParameters jobParameters = new JobParametersBuilder().addLong("Stared Date", System.currentTimeMillis()).toJobParameters();		
		try {
			jobLauncher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Done...!";
	}
	
	
//	CREATE TABLE CUSTOMER (ID NUMBER ,FIRSTNAME VARCHAR2(50),LASTNAME VARCHAR2(50),EMAIL VARCHAR2(50),GENDER VARCHAR2(5),CONTACTNO VARCHAR2(50),COUNTRY VARCHAR2(50) ,DOB VARCHAR2(50));

}

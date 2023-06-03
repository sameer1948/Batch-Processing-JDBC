package com.spring.batch.jdbc.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.spring.batch.jdbc.entity.Customer;
import com.spring.batch.jdbc.listener.MyJobListener;
import com.spring.batch.jdbc.processor.CustomerProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Bean
	public FlatFileItemReader<Customer> reader(){
		FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
		
		flatFileItemReader.setResource(new ClassPathResource("customers.csv"));
		flatFileItemReader.setLineMapper(new DefaultLineMapper<Customer>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setDelimiter(DELIMITER_COMMA);
						setNames("id","firstName","lastName","email","gender","contactNo","country","dob");
					}
					
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Customer>(){
					{
						setTargetType(Customer.class);
					}
					
				});
			
			}
		});
		
		return flatFileItemReader;
	}
	
	@Bean
	public ItemProcessor<Customer, Customer> processor(){
		//new CustomerProcessor();		
		return cust->cust;
	}
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public JdbcBatchItemWriter<Customer> writer(){		
		JdbcBatchItemWriter<Customer> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(dataSource);
		jdbcBatchItemWriter.setSql("INSERT INTO CUSTOMER(ID,FIRSTNAME,LASTNAME,EMAIL,GENDER,CONTACTNO,COUNTRY,DOB) VALUES (:ID,:FIRSTNAME,:LASTNAME,:EMAIL,:GENDER,:CONTACTNO,:COUNTRY,:DOB)");
		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		return jdbcBatchItemWriter;
	}
	
	@Bean
	public JobExecutionListener listener() {
		return new MyJobListener();
	}
	
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step myStep() {		
		return stepBuilderFactory.get("MySetp")
				                 .<Customer,Customer>chunk(10)
								 .reader(reader())
								 .processor(processor())
								 .writer(writer())
								 .build();
	}
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Bean
	public Job myJob() {
		return jobBuilderFactory.get("MyJob")
								.incrementer(new RunIdIncrementer())
								.listener(listener())
								.start(myStep())
								.build();
	}
	

}

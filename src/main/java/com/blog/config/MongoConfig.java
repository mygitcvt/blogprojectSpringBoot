package com.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

@Configuration
public class MongoConfig {

	@Bean
	public MongoTemplate mongoTemplate() {
		System.out.println("in mongotemplate");
		MongoTemplate mongoTemplate = new MongoTemplate(
				
				new SimpleMongoDbFactory(new MongoClient("localhost", 27017), "COREVALUE"));

		return mongoTemplate;

	}

}
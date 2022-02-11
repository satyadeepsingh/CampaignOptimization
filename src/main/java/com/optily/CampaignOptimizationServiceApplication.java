package com.optily;

import com.optily.repository.CampaignGroupRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class CampaignOptimizationServiceApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(CampaignOptimizationServiceApplication.class, args);
		context.getBean(CampaignGroupRepo.class).loadCsvTestData();
		log.info("Bootstrapped with data: {}", context.getBean(CampaignGroupRepo.class).getAllCampaignGroups());
	}

}

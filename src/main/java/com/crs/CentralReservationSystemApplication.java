package com.crs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.crs","com.common"})
public class CentralReservationSystemApplication {

	private static final Logger logger = LoggerFactory.getLogger(CentralReservationSystemApplication.class);

	public static void main(String[] args) {
		setSpringProfile();
		System.setProperty("file.encoding", "UTF-8");
		SpringApplication.run(CentralReservationSystemApplication.class, args);
	}

	static void setSpringProfile() {
		String profile = System.getProperty("spring.profile.active");

		if (profile == null || profile.isEmpty()) {
			System.setProperty("spring.profile.active", "local");
		} else if (profile.equalsIgnoreCase("local") || profile.equalsIgnoreCase("junit")
				|| profile.equalsIgnoreCase("test")) {
		} else {
			System.setProperty("spring.profile.active", "production");
		}
		logger.info("Profile : {}", profile);
	}

}

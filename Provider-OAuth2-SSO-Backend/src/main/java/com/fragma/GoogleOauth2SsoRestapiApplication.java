package com.fragma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GoogleOauth2SsoRestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleOauth2SsoRestapiApplication.class, args);
	}

}

package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringRestDocsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestDocsDemoApplication.class, args);
	}

	/*@Bean
	public FilterRegistrationBean myFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new ShallowEtagHeaderFilter());
		registration.addInitParameter("writeWeakETag", "false");
		registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
		return registration;
	}*/
}

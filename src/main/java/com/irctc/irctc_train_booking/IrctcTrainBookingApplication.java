package com.irctc.irctc_train_booking;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IrctcTrainBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrctcTrainBookingApplication.class, args);
		System.out.println("Working");
	}

}

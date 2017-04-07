package com.megatech.iot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SimulateRestController {
	
	@RequestMapping("/")
	String home() {
		System.out.println("Simulation Started");
		SimulateController.simulateData();
		return "Simulating Data";
	}

}

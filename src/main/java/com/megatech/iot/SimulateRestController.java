package com.megatech.iot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SimulateRestController {
	
	SimulateController simu=new SimulateController();
	@RequestMapping("/")
	String home() {
		System.out.println("Simulation Started");
		simu.simulateData();
		return "Simulating Data";
	}

}

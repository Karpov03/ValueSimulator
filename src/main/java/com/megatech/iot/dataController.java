package com.megatech.iot;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class dataController {
	
	
	
	
	
	@RequestMapping(value = "/")
	public String adddata() {
		
		
		
		return "Welcome";
	}
	
	@RequestMapping(value = "/pushdata")
	public String pushdata() {
		
		Controller.simulatedata();
		
		return "Pushing Data";
	}
	
	
	
}
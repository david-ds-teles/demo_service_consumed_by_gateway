package com.david.ds.teles.demo_account;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

	@RequestMapping("/hello")
	public String hello() {
		return "Hello from Account Service";
	}
}

package com.springboot.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TVSeriesController {

	@RequestMapping("/tvseries")
	public List<String> getSeriesDetails() {
		List<String> serials = new ArrayList<>();
		serials.add("Twilight");
		serials.add("True Blood");
		serials.add("The Vampire DAIRIES");
		serials.add("Stranger Things");
		serials.add("GOT");
		serials.add("Friends");
		serials.add("Two and a half men");
		return serials;
	}
}

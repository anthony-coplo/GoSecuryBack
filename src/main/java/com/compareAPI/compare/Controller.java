package com.compareAPI.compare;

import java.util.Collection;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Entity.Image;
import Entity.Ressources;

@RestController
@RequestMapping("/ressources")
@CrossOrigin(value = "*")
public class Controller {
	
	@GetMapping
	public Collection<Ressources> getRessources() throws Exception {
		System.out.println("ok");
		connect connect = new connect();
		connect.Connection();
		
		Collection<Ressources> ressource = connect.getRessources();
	
		return ressource;
	}
	
}

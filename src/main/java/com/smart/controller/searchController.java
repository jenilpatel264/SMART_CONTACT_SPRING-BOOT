package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.DAO.contactDAO;
import com.smart.DAO.userDAO;
import com.smart.VO.*;

@RestController
public class searchController {
	@Autowired userDAO userDAO;
	@Autowired contactDAO contactDAO;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(Principal principal,@PathVariable("query") String query)
	{
		System.out.println(query);
		userVO userVO=this.userDAO.getUserByName(principal.getName());
		List<contactVO> contacts=this.contactDAO.findByNameContaining(query);
		return ResponseEntity.ok(contacts);
		
	}
}

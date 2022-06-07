package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.DAO.userDAO;
import com.smart.VO.userVO;

@Controller
@RequestMapping("/user")
public class userController {
	@Autowired
	private userDAO userDAO;

	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal) {
		String username=principal.getName();
		userVO userVO=this.userDAO.getUserByName(username);
		System.out.println(userVO);
		model.addAttribute("user",userVO);
		return "user/user_dashboard";
	}
}

package com.smart.controller;

import javax.naming.Binding;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smart.DAO.userDAO;
import com.smart.VO.userVO;
import com.smart.services.userServices;
import com.smart.helper.*;
@Controller
public class homeController {
	@Autowired
	static BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired userDAO userDAO;
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public String test(Model model)
	{
		model.addAttribute("title","jenil patel");
		return "home";
	}
	@RequestMapping("/about")
	public String about()
	{
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title","sign up here");
		model.addAttribute("user",new userVO());
		return "signup"; 
	}
	
	@RequestMapping(value = "/do_register",method=RequestMethod.POST)
	public  String registerUser(@Valid @ModelAttribute("user") userVO userVO,BindingResult bindingResult1	,@RequestParam(value = "agreement",defaultValue = "false") boolean agreement,Model model,HttpSession httpSession)


	
	{
		try {
			
			if(!agreement)
			{
				System.out.println("you are not login");
				throw new Exception("you are not tick on agreement");
			}
			
			if(bindingResult1.hasErrors())
			{
				System.out.println("dsadasda");
				model.addAttribute("userVO",userVO);
				return "signup";
			}
			userVO.setRole("ROLE_USER");
			userVO.setEnabled(true);
			
			BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
		
			CharSequence cs=userVO.getPassword();
			String s1=cs.toString();
			userVO.setPassword(bCryptPasswordEncoder.encode(s1));
			
		
			userVO.setImgUrl("default.png");
			model.addAttribute("user",userVO);
		    this.userDAO.save(userVO);

			System.out.println(agreement);
			System.out.println(userVO);
			model.addAttribute("userVO",new userVO());
			httpSession.setAttribute("message", new Message("successful register!!","alert-success"));
			return "login";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("userVO",userVO);
			httpSession.setAttribute("message", new Message("something went wrong!!","alert-danger"));
			return "signup";
		}
		
	}
	
	@GetMapping("/signin")
	public String login(Model model)
	{
//		model.addAttribute("title","login page");
		return "login";
	}
	
}

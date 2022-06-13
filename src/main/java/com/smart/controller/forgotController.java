package com.smart.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.DAO.userDAO;
import com.smart.VO.userVO;
import com.smart.services.emailservice;

@Controller
public class forgotController {
	@Autowired
	private emailservice emailservice;
	
	@Autowired
	private userDAO userDAO;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	Random random=new Random(10000);
	
	@RequestMapping("/forgot")
	public String openmailform()
	{
		return "forgot_email_form";
	} 
	
	@PostMapping("/send-otp")
	public String sendotp(@RequestParam("email") String email,HttpSession httpSession)
	{
		System.out.println(email);
		
		int otp= (int) Math.floor(1000 + Math.random() * 1000);
		System.out.println(otp);
		String message="OTP = "+otp;
		
		boolean flag=this.emailservice.sendEmail("OTP FROM SYSTEM", message, email);
		if(flag)
		{
			httpSession.setAttribute("myotp",otp);
			httpSession.setAttribute("email", email);
			/* mdxmgfqavrbrstqc */
			return "/varify";
		}
		else
		{
			httpSession.setAttribute("message","Check your Email");
			return "forgot_email_form";
		}
		
	}
	@PostMapping("/verify-otp")
	public String verifyotp(@RequestParam("otp") int otp,HttpSession httpSession)
	{
		int myotp=(Integer) httpSession.getAttribute("myotp");
		String email=(String) httpSession.getAttribute("email");
		if(myotp==otp)
		{
			userVO userVO=this.userDAO.getUserByName(email);
			if(userVO==null)
			{
				httpSession.setAttribute("message","User is not exist");
				return "varify";
			}
			else
			{
				return "password_change_form";
			}
			
		}
		else
		{
			httpSession.setAttribute("message","You have entered invalid OTP");
			return "varify";
		}
	
		
		
	}
	
	@PostMapping("change-password")
	public String changepassword(HttpSession httpSession,@RequestParam("newpassword") String newpassword)
	{
		String email=(String) httpSession.getAttribute("email");
		userVO userVO=this.userDAO.getUserByName(email);
		userVO.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		this.userDAO.save(userVO);
		
		return "redirect:/signin?change=password changed successfully";
	}

}

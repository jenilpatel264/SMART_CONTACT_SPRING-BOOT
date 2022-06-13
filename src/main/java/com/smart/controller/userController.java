package com.smart.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.smart.VO.*;
import com.smart.helper.*;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smart.DAO.contactDAO;
import com.smart.DAO.userDAO;
import com.smart.VO.*;
import com.smart.helper.*;
import com.smart.config.*;

@Controller
@RequestMapping("/user")
public class userController {
	@Autowired
	private userDAO userDAO;
	
	@Autowired
	private contactDAO contactDAO;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	

	@ModelAttribute
	public void addcommonData(Model model, Principal principal) {
		String username = principal.getName();
		userVO userVO = this.userDAO.getUserByName(username);

		System.out.println(userVO);
		model.addAttribute("user", userVO);
	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		return "user/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openForm(Model model) {
		model.addAttribute("title", "Add Contact");
		System.out.println();
		model.addAttribute("contactVO", new contactVO());
		return "user/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processcontact(@ModelAttribute contactVO contactVO, Principal principal,@RequestParam("profileImage") MultipartFile file,HttpSession httpSession) {
		try {

			String name = principal.getName();
			userVO userVO = this.userDAO.getUserByName(name);
			contactVO.setUserVO(userVO);
			
			if(file.isEmpty())
			{
				System.out.println("not uploaded");
//				contactVO.setImg("download111.png");
			}
			else
			{
				contactVO.setImg(file.getOriginalFilename());
				java.io.File saveFile=new ClassPathResource("static/image").getFile();
				Path path=Paths.get(saveFile.getAbsoluteFile()+java.io.File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image uploaded");
			}
			
			userVO.getContactss().add(contactVO);
			this.userDAO.save(userVO);
			System.out.println("updated");
			System.out.println("dasda");
			httpSession.setAttribute("message", new Message("YOUR CONTACT IS ADDED","success"));
			return "user/add_contact_form";
			
		} catch (Exception e) {
			httpSession.setAttribute("message", new Message("PLEASE TRY AGAIN !! SOMETHING WENT WRONG","danger"));
			e.printStackTrace();
			return "user/add_contact_form";
		}
		
	}
	@GetMapping("show-contacts/{page}")
	public String showContacts(@PathVariable("page")Integer page,Model model,Principal principal)
	{
		String name=principal.getName();
		userVO userVO=this.userDAO.getUserByName(name);
		Pageable pageable=PageRequest.of(page, 5);
		Page<contactVO> contacts=this.contactDAO.findContactsByUser(userVO.getId(),pageable);
		
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentpage",page);
		model.addAttribute("totalpages",contacts.getTotalPages());
//		List<contactVO> contacts=userVO.getContactss();
		return "user/show_contacts";
	}
	
	@RequestMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer cid,Model model,Principal principal)
	{
		System.out.println(cid);
		String name=principal.getName();
		userVO userVO=this.userDAO.getUserByName(name);
		Optional<contactVO> contact=this.contactDAO.findById(cid);
		contactVO contactVO=contact.get();
		
		if(userVO.getId()==contactVO.getUserVO().getId())
		{
			model.addAttribute("contact",contactVO);
		}
		
		return "user/contact_detail";
	}
	
	@GetMapping("/delete/{cid}")
	public String delete_contact(@PathVariable("cid") Integer cid,Model model,HttpSession httpSession)
	{
		Optional<contactVO> find=this.contactDAO.findById(cid);
		contactVO contactVO=find.get();
		contactVO.setUserVO(null);
		this.contactDAO.delete(contactVO);
		httpSession.setAttribute("message", new Message("Contact Delete successfully", "success"));
		return "redirect:/user/show-contacts/0";
		
	}
	
	@PostMapping("/update-contact/{cid}")
	public String updatecontact(@PathVariable("cid") Integer cid,Model model)
	{
		contactVO contactVO=this.contactDAO.findById(cid).get();
		model.addAttribute("contact",contactVO);
		return "user/update_form";
	}
	
	@PostMapping("/process-update")
	public String update(@ModelAttribute contactVO contactVO, Principal principal,@RequestParam("profileImage") MultipartFile file,HttpSession httpSession)
	{
		try {
			
			contactVO oldcontactdetails=this.contactDAO.findById(contactVO.getCid()).get();
			if(!file.isEmpty())
			{
				java.io.File deleteFile=new ClassPathResource("static/image").getFile();
				java.io.File file1=new java.io.File(deleteFile, oldcontactdetails.getName());
				file1.delete();
				
				
				java.io.File saveFile=new ClassPathResource("static/image").getFile();
				Path path=Paths.get(saveFile.getAbsoluteFile()+java.io.File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				contactVO.setImg(file.getOriginalFilename());
				System.out.println("image uploaded");
			}
			else
			{
				contactVO.setImg(oldcontactdetails.getImg());
			}
			
			userVO userVO=this.userDAO.getUserByName(principal.getName());
			contactVO.setUserVO(userVO);
			this.contactDAO.save(contactVO);
			httpSession.setAttribute("message", new Message("your contact is updated", "success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		System.out.println(contactVO.getName());
		return "redirect:/user/show-contacts/0";
	}
	
	@GetMapping("/profile")
	public String profile()
	{
		return "user/profile";
	}
	
	@GetMapping("/setting")
	public String setting()
	{
		return "user/setting";
	}
	
	@PostMapping("/change-password")
	public String changepassword(@RequestParam("oldpassword") String oldpassword,@RequestParam("newpassword") String newpassword,Principal principal,HttpSession httpSession)
	{
		String name=principal.getName();
		userVO userVO=this.userDAO.getUserByName(name);
		if(this.bCryptPasswordEncoder.matches(oldpassword, userVO.getPassword()))
		{
			userVO.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.userDAO.save(userVO);
			httpSession.setAttribute("message", new Message("Successfully changed your password","success"));
			return "redirect:/user/setting";
		}
		else
		{
			httpSession.setAttribute("message", new Message("Please!! enter correct your Old Password","danger"));
			
			return "redirect:/user/setting";
		}
		
		
		
	}
}

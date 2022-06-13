package com.smart.services;





import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

@Service
public class emailservice {
	
	public boolean sendEmail(String subject , String message , String to)
	{
		boolean b=false;
		String host="smtp.gmail.com";
	Properties properties=System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		
		
		Session session=javax.mail.Session.getInstance(properties, new Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication()
			{
				return new javax.mail.PasswordAuthentication("pateljenil717@gmail.com", "mdxmgfqavrbrstqc");
			}
		});
		session.setDebug(true);
		
		
		MimeMessage mimeMessage=new MimeMessage(session);
		
	try {
		String from="pateljenil717@gmail.com";
		mimeMessage.setFrom(new InternetAddress("pateljenil717@gmail.com"));
		mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
		mimeMessage.setSubject(subject);
		mimeMessage.setText(message);
		Transport.send(mimeMessage);
		System.out.println("successfull");
		return true;
		
	
		
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	return false;	
		
		
	}

}

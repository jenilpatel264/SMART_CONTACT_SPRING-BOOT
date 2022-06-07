package com.smart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.DAO.userDAO;
@Service
public class userServices {
	@Autowired userDAO userDAO;

}

package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.smart.DAO.*;
import com.smart.VO.userVO;

public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private userDAO userDAO;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		userVO userVO =userDAO.getUserByName(username);
		if(userVO==null)
		{
			throw new UsernameNotFoundException("not found user");
		}
		customUserDetails customUserDetails=new customUserDetails(userVO);
		
		
		return customUserDetails;
	}

}

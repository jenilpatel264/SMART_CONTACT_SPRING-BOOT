package com.smart.DAO;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.smart.VO.userVO;
@Repository
public interface userDAO extends JpaRepository<userVO, Integer> {
	@Query("select u from userVO u where u.email =:email")
	public userVO getUserByName(@Param("email") String email);
}

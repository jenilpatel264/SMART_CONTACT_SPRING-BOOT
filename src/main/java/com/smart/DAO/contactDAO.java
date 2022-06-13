package com.smart.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.VO.contactVO;
import com.smart.VO.userVO;

public interface contactDAO extends JpaRepository<contactVO, Integer> {
	
	@Query("from contactVO as c where c.userVO.id=:userId")
	public Page<contactVO> findContactsByUser(@Param("userId")int userId,Pageable pageable);
	
	public List<contactVO> findByNameContaining(String name);

}

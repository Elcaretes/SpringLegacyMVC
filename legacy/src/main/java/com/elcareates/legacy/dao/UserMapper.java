package com.elcareates.legacy.dao;

import java.util.List;

import com.elcareates.legacy.vo.BbsVO;
import com.elcareates.legacy.vo.UserVO;

public interface UserMapper {

	public int insertUser(UserVO param);
	public UserVO loginUser(UserVO param);
	
	public List<BbsVO> selectAllBbs(int pNum);
	public List<BbsVO> selectBbs(int bNum);
	public int rowCount();
	public int insertBbs(BbsVO param);
	public int updateBbs(BbsVO param);
	
	
}//
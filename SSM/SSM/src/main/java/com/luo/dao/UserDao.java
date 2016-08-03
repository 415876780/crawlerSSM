package com.luo.dao;

import com.luo.domain.User;

public interface UserDao {
	public User selectUserById(Integer userId);
}

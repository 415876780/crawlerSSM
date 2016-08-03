package ccnu.computer.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ccnu.computer.dao.IUserDao;
import ccnu.computer.model.Pager;
import ccnu.computer.model.User;
import ccnu.computer.model.UserException;
@Service("userService")
public class UserService implements IUserService {
	private IUserDao userDao;
	
	public IUserDao getUserDao() {
		return userDao;
	}
	@Resource
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
		User u = userDao.loadByUsername(user.getUsername());
		if(u!=null) throw new UserException("要添加的用户已经存在");
		userDao.add(user);
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		userDao.delete(id);
	}

	@Override
	public User load(int id) {
		// TODO Auto-generated method stub
		return userDao.load(id);
	}

	@Override
	public List<User> list() {
		// TODO Auto-generated method stub
		return userDao.list();
	}

	@Override
	public Pager<User> find() {
		// TODO Auto-generated method stub
		return userDao.find();
	}

	@Override
	public User login(String username, String password) {
		// TODO Auto-generated method stub
		

		
		
		
		User  u = userDao.loadByUsername(username);
		if(u==null) throw new UserException("登陆用户不存在");
		
		if(!u.getPassword().equals(password))throw new UserException("用户密码错误");
		return u;
	}

}

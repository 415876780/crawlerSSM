package ccnu.computer.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ccnu.computer.dao.IPosterDao;
import ccnu.computer.dao.IUserDao;
import ccnu.computer.model.Pager;
import ccnu.computer.model.Poster;
import ccnu.computer.model.User;
import ccnu.computer.model.UserException;
@Service("weiBoService")
public class WeiBoService implements IWeiBoService {
	private IPosterDao posterDao;
	
	public IPosterDao getPosterDao() {
		return posterDao;
	}
	@Resource
	public void setPosterDao(IPosterDao posterDao) {
		this.posterDao = posterDao;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		posterDao.delete(id);
	}

	@Override
	public Poster load(int id) {
		// TODO Auto-generated method stub
		return posterDao.load(id);
	}

	@Override
	public List<Poster> list() {
		// TODO Auto-generated method stub
		return posterDao.list();
	}

	@Override
	public Pager<Poster> find() {
		// TODO Auto-generated method stub
		return posterDao.find();
	}
	
	@Override
	public Pager<Poster> finds() {
		// TODO Auto-generated method stub
		return posterDao.finds();
	}

	@Override
	public void add(Poster poster) {
		// TODO Auto-generated method stub
		/*Poster u = posterDao.loadByUsername(poster.getTitle());
		if(u!=null) throw new UserException("要添加的用户已经存在");
		posterDao.add(poster);*/
	}
	@Override
	public void update(Poster poster) {
		// TODO Auto-generated method stub
		posterDao.update(poster);
	}

}

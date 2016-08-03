package ccnu.computer.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ccnu.computer.dao.ITextDao;
import ccnu.computer.dao.IUserDao;
import ccnu.computer.model.Pager;
import ccnu.computer.model.Text;
import ccnu.computer.model.User;
import ccnu.computer.model.UserException;
@Service("textService")
public class TextService implements ITextService {
	private ITextDao textDao;
	
	public ITextDao getTextDao() {
		return textDao;
	}
	@Resource
	public void setTextDao(ITextDao textDao) {
		this.textDao = textDao;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		textDao.delete(id);
	}

	@Override
	public Text load(int id) {
		// TODO Auto-generated method stub
		return textDao.load(id);
	}

	@Override
	public List<Text> list() {
		// TODO Auto-generated method stub
		return textDao.list();
	}

	@Override
	public Pager<Text> find() {
		// TODO Auto-generated method stub
		return textDao.find();
	}
	
	@Override
	public Pager<Text> finds() {
		// TODO Auto-generated method stub
		return textDao.finds();
	}

	@Override
	public void add(Text text) {
		// TODO Auto-generated method stub
		Text u = textDao.loadByUsername(text.getTitle());
		if(u!=null) throw new UserException("要添加的用户已经存在");
		textDao.add(text);
	}
	@Override
	public void update(Text text) {
		// TODO Auto-generated method stub
		textDao.update(text);
	}

}

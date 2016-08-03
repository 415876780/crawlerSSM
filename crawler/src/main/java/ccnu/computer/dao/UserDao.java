package ccnu.computer.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import ccnu.computer.model.Pager;
import ccnu.computer.model.SystemContext;
import ccnu.computer.model.User;
@Repository("userDao")
public class UserDao extends HibernateDaoSupport  implements IUserDao {
	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory){
		this.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().save(user);
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().update(user);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		User user= this.load(id);
		this.getHibernateTemplate().delete(user);
	}

	@Override
	public User load(int id) {
		// TODO Auto-generated method stub
		return (User)this.getHibernateTemplate().load(User.class, id);
	}

	@Override
	public List<User> list() {
		// TODO Auto-generated method stub
		return this.getSession().createQuery("from User").list();
	}

	@Override
	public Pager<User> find() {
		// TODO Auto-generated method stub
		int size = SystemContext.getSize();
		int offset = SystemContext.getOffset();
		Query query= this.getSession().createQuery("from User");
		query .setFirstResult(offset).setMaxResults(size);
		List<User> datas = query.list();
		Pager<User> us= new Pager<User>();
		us.setDatas(datas);
		us.setOffset(offset);
		us.setSize(size);
		long total = (Long)this .getSession().createQuery("select count(*) from User").uniqueResult();
		System.out.println(total);
		System.out.println(us.getDatas());
		us.setTotal(total);
		return us;
	}

	@Override
	public User loadByUsername(String name) {
		// TODO Auto-generated method stub
		return (User)this.getSession().createQuery("from User where username=?")
				.setParameter(0, name).uniqueResult();
	}

}

package ccnu.computer.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import ccnu.computer.model.Pager;
import ccnu.computer.model.SystemContext;
import ccnu.computer.model.Poster;
@Repository("posterDao")
public class PosterDao extends HibernateDaoSupport implements IPosterDao{

	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void add(Poster poster) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().save(poster);
	}

	@Override
	public Poster load(int id) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().load(Poster.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Poster> list() {
		// TODO Auto-generated method stub
		return this.getSession().createQuery("from Poster").list();
	}

	@Override
	public Pager<Poster> find() {
		// TODO Auto-generated method stub
		int size = SystemContext.getSize();
		int offset = SystemContext.getOffset();
		System.out.println("========================================");
		//String hql = "from Poster where isLabel = :n";  
		Query query = this.getSession().createQuery("from Poster where isLabel is null");
				//.setParameter(0, "NULL");
		//Query query = this.getSession().createQuery(hql);
		//query.setParameter("n","已标记");
		query.setFirstResult(offset).setMaxResults(size);
		List<Poster> datas = query.list();
		System.out.println(datas.size());
		Pager<Poster> us = new Pager<Poster>();
		us.setDatas(datas);
		us.setOffset(offset);
		us.setSize(size);
		long total = (Long)this.getSession()
				.createQuery("select count(*) from Poster where isLabel is null")
				.uniqueResult();
		System.out.println(total);
		us.setTotal(total);
		return us;
	}
	@Override
	public Pager<Poster> finds() {
		// TODO Auto-generated method stub
		int size = SystemContext.getSize();
		int offset = SystemContext.getOffset();
		Query query = this.getSession().createQuery("from Poster");
		query.setFirstResult(offset).setMaxResults(size);
		List<Poster> datas = query.list();
		Pager<Poster> us = new Pager<Poster>();
		us.setDatas(datas);
		us.setOffset(offset);
		us.setSize(size);
		long total = (Long)this.getSession()
					.createQuery("select count(*) from Poster").uniqueResult();
		us.setTotal(total);
		return us;
	}
	
	@Override
	public Poster loadByUsername(String title) {
		// TODO Auto-generated method stub
		return (Poster) this.getSession().createQuery("from Poster where username=?")
				.setParameter(0, title).uniqueResult();
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		Poster poster = this.load(id);
		this.getHibernateTemplate().delete(poster);
	}

	@Override
	public void update(Poster poster) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().update(poster);
	}

}

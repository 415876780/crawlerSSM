package ccnu.computer.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import ccnu.computer.model.Pager;
import ccnu.computer.model.SystemContext;
import ccnu.computer.model.Text;
@Repository("textDao")
public class TextDao extends HibernateDaoSupport implements ITextDao{

	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void add(Text text) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().save(text);
	}

	@Override
	public Text load(int id) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().load(Text.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Text> list() {
		// TODO Auto-generated method stub
		return this.getSession().createQuery("from Text").list();
	}

	@Override
	public Pager<Text> find() {
		// TODO Auto-generated method stub
		int size = SystemContext.getSize();
		int offset = SystemContext.getOffset();
		Query query = this.getSession().createQuery("from Text where isLabel<>?")
				.setParameter(0, "已标记");
		query.setFirstResult(offset).setMaxResults(size);
		List<Text> datas = query.list();
		Pager<Text> us = new Pager<Text>();
		us.setDatas(datas);
		us.setOffset(offset);
		us.setSize(size);
		long total = (Long)this.getSession()
					.createQuery("select count(*) from Text where isLabel<>?").setParameter(0, "已标记")
					.uniqueResult();
		System.out.println("total:"+total);
		us.setTotal(total);
		return us;
	}
	@Override
	public Pager<Text> finds() {
		// TODO Auto-generated method stub
		int size = SystemContext.getSize();
		int offset = SystemContext.getOffset();
		Query query = this.getSession().createQuery("from Text");
		query.setFirstResult(offset).setMaxResults(size);
		List<Text> datas = query.list();
		Pager<Text> us = new Pager<Text>();
		us.setDatas(datas);
		us.setOffset(offset);
		us.setSize(size);
		long total = (Long)this.getSession()
					.createQuery("select count(*) from Text").uniqueResult();
		System.out.println("total:"+total);
		us.setTotal(total);
		return us;
	}
	
	@Override
	public Text loadByUsername(String title) {
		// TODO Auto-generated method stub
		return (Text) this.getSession().createQuery("from Text where username=?")
				.setParameter(0, title).uniqueResult();
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		Text text = this.load(id);
		this.getHibernateTemplate().delete(text);
	}

	@Override
	public void update(Text text) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().update(text);
	}

}

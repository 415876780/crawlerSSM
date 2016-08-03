package ccnu.computer.dao;

import java.util.List;

import ccnu.computer.model.Pager;
import ccnu.computer.model.Poster;

public interface IPosterDao {
	public void add(Poster poster);
	public Poster load(int id);
	public List<Poster> list();
	public Pager<Poster> find();
	public Pager<Poster> finds();
	public Poster loadByUsername(String title);
	public void update(Poster poster);
	public void delete(int id);
}

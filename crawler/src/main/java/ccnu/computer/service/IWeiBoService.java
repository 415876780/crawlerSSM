package ccnu.computer.service;

import java.util.List;

import ccnu.computer.model.Pager;
import ccnu.computer.model.Poster;

public interface IWeiBoService {
	public void add(Poster poster);
	public void update(Poster poster);
	public void delete(int id);
	public Poster load(int id);
	public List<Poster> list();
	public Pager<Poster> find();
	public Pager<Poster> finds();
	
}

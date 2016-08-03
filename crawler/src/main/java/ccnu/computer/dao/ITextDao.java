package ccnu.computer.dao;

import java.util.List;

import ccnu.computer.model.Pager;
import ccnu.computer.model.Text;

public interface ITextDao {
	public void add(Text text);
	public Text load(int id);
	public List<Text> list();
	public Pager<Text> find();
	public Pager<Text> finds();
	public Text loadByUsername(String title);
	public void update(Text text);
	public void delete(int id);
}

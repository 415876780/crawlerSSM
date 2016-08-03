package ccnu.computer.service;

import java.util.List;

import ccnu.computer.model.Pager;
import ccnu.computer.model.Text;
import ccnu.computer.model.User;

public interface ITextService {
	public void add(Text text);
	public void update(Text text);
	public void delete(int id);
	public Text load(int id);
	public List<Text> list();
	public Pager<Text> find();
	public Pager<Text> finds();
	
}

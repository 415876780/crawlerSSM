package ccnu.computer.crawler;

import java.io.IOException;
import java.util.Set;
import javax.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ccnu.computer.dao.TextDao;

public abstract class AbstractParser implements Parser {
    //@Resource
   // public TextDao textDao = new TextDao();
/*    private Set<String> urls;
    public void setUrls(Set<String> urls){
    	this.urls=urls;
    }
	public Set<String> getUrls() {
		return urls;
	}
	*/
    @Override
	public  Document getDocs(String url) {
		Document doc = null;
		try {
			doc = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.1.141.64 Safari/537.31")
					.timeout(100000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}
		return doc;
	}
   
/*    public  void run(Document doc,String u,String t) {
    	parse(doc,u,t);
    }*/
}
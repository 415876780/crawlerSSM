package ccnu.computer.crawler;

import java.util.Arrays;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import ccnu.computer.model.Text;
import ccnu.computer.summary.HyperGraph;
import ccnu.computer.util.CrawlerUtil;

/*
 * 对于一个新闻网页，爬取相关内容分为以下几步：
 * （1）首先是拼凑一个搜索网页的网址，例如："http://search.sina.com.cn/?q="+ URLEncoder.encode(title, "utf-8")+"&c=news&from=channel&ie=utf-8";
 * （2）其次是获取第一步获取了多少个记录，一般换算成页来计算，方便于下一步数据 的爬取
 * （3）通过第二步获取的页数来拼成多个网址
 * （4）获取第三步中每一个网址上的新闻链接
 * （5）通过第四部的新闻链接网址获取新闻的内容
 * 
 * 
 * */

@Component("sinaNewsParser")
public class SinaNewsParser extends AbstractNewsParser {

	@Override
	public void parse(Document doc, String url,String topic) {
		// TODO Auto-generated method stub
		Text text= new Text();
		String title = CrawlerUtil.getTitle(doc, "title");
		text.setUrl(url);
		text.setTopic(topic);
		text.setTitle(title);
		System.out.println(title);
		String time = CrawlerUtil.getTime(doc, Arrays.asList(new String[]{"time-source","artInfo"}));
		text.setDate(time);
		System.out.println(time);
		String content = CrawlerUtil.getContent(doc, Arrays.asList(new String[]{"#artibody","td.l17"}));
		text.setContent(content);
		System.out.println(content);
		text.setIsLabel("未标记");
		//text.setSummary(HyperGraph.getSummary(content, 200));
		//textDao= new TextDao();
		if(!"".equals(content)){
			System.out.println("======================+++++++++===="+textDao==null);
			textDao.add(text);
		}
		text=null;
	}

	@Override
	public String getUrl(String title){
		return getUrl(title,"http://search.sina.com.cn/?q=","&c=news&from=channel&ie=utf-8");
	}
	@Override
	public String getUrl(String title,int page){
		return getUrl(title,page,"http://search.sina.com.cn/?q=",
		        		"&c=news&from=channel&ie=utf-8&col=&range=&source=&country=&size=&time=&a=&page=","&pf=2131425461&ps=2134309112&dpc=1");
	}

	@Override
	public int getUrlsize(String url) {
		// TODO Auto-generated method stub
		return getUrlsize(url, "div.l_v2");
	}

	@Override
	public Set<String> getItemUrl(String url) {
		// TODO Auto-generated method stub
		return getItemUrl(url, "http://(finance|news).sina.com.cn.*","div.result");
	}
}

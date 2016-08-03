package ccnu.computer.crawler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import ccnu.computer.dao.PosterDao;
import ccnu.computer.dao.TextDao;
import ccnu.computer.model.Poster;
import ccnu.computer.net.HttpRequesterImpl;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;


@Component("weiBoParser")
public class WeiBoParser extends AbstractParser {
	
	public PosterDao posterDao;
	private String cookie=null;
	
	public PosterDao getPosterDao() {
		return posterDao;
	}
	@Resource
	public void setPosterDao(PosterDao posterDao) {
		this.posterDao = posterDao;
	}
	public WeiBoParser(){

	}
	public Document getDocs(String url) {
		HttpResponse response = null;
		if(this.cookie==null){
			try {
				 cookie = WeiboCN.getSinaCookie("415876780@qq.com", "yanjie123");
				//cookie = WeiboCN.getSinaCookie(this.getName(), this.getPassword());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			HttpRequest request = new HttpRequest(new CrawlDatum(url));
			request.setCookie(cookie);
			response = request.getResponse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(response.getHtml("utf-8"));
		//System.out.println(doc);
		return doc;
	}
	

	
	@Override
	public void parse(Document doc, String baseTag, String topic) {
		// TODO Auto-generated method stub
		Elements weibos = doc.select("div.c");
		//textDao= new TextDao();
		for (Element weibo : weibos) {
			Poster poster =extractToPoster(weibo);
			
			if(poster!=null){
				poster.setTopic(topic);
				System.out.println("姝ｅ湪鍐欏叆鏁版嵁搴?...");
				posterDao.add(poster);
				
				poster=null;
			}
			
			
		}
	}

	private Poster extractToPoster(Element element) {
		// TODO Auto-generated method stub
		Poster poster = new Poster();
		int divSize = element.getElementsByTag("div").size();
		int aSize = element.getElementsByTag("div").get(divSize - 1)
				.getElementsByTag("a").size();
		int flag = 1;
		while (flag < aSize) {
			if ("鏀惰棌".equals(element.getElementsByTag("div").get(divSize - 1)
					.getElementsByTag("a").get(aSize - flag).text())) {
				break;
			} else {
				flag++;
			}
		}
		if (aSize > 4) {
			String a1 = extractNumber(element.getElementsByTag("div")
					.get(divSize - 1).getElementsByTag("a")
					.get(aSize - flag - 3).text());// 鐐硅禐鏁?
			String a2 = extractNumber(element.getElementsByTag("div")
					.get(divSize - 1).getElementsByTag("a")
					.get(aSize - flag - 2).text());// 杞彂鏁?
			String a3 = extractNumber(element.getElementsByTag("div")
					.get(divSize - 1).getElementsByTag("a")
					.get(aSize - flag - 1).text());// 璇勮鏁?
			String a4 = element.getElementsByClass("nk").text();// 鍗氫富鍚?
			String a5 = element.getElementsByClass("ctt").text();// 寰崥鍐呭
			String a6 = extractTime(element.getElementsByClass("ct").text());
			poster.setAgreement(a1);
			poster.setTransmit(a2);
			poster.setComment(a3);
			poster.setUser(a4);
			poster.setContent(a5);
			poster.setTitle(this.getTitle(a5));
			poster.setTime(a6);
			//System.out.println(a1+":"+a2+":"+a3+":"+a4+":"+a5+":"+a6);
		}else{
			return null;
		}
		return poster;
	}
	public String getTitle(String content){
		Pattern pattern = Pattern.compile("#.*#");
		Matcher m = pattern.matcher(content);
		String title="";
		while (m.find()) {
			if (!"".equals(m.group())) {
				title = m.group();
				break;
			}
		}
		return title.replaceAll("#", "");
	}
	
	public String extractTime(String time) {
		String[] s= time.split("聽");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<s.length-1;i++){
			sb.append(s[i]);
		}
		return sb.toString();
	}
	
	public String extractNumber(String content) {
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(content);
		String result = null;
		while (m.find()) {
			if (!"".equals(m.group())) {
				result = m.group();
			}
		}
		return result;
	}
	@Override
	public int getUrlsize(String url) {
		// TODO Auto-generated method stub
		Document doc = getDocs(url);
		String sizetext=doc.select("span.cmt").text();
		System.out.println(sizetext);
		sizetext=sizetext.replaceAll("\\D", "");
		int size=Integer.parseInt(sizetext);
		if(size/10>99){
			return 100;
		}else{
			return Integer.parseInt(sizetext)/10+1;
		}
		
		
	}

	@Override
	public Set<String> getItemUrl(String url) {
		// TODO Auto-generated method stub
		Set<String> urls= new HashSet<String>();
		urls.add(url);
		return urls;
	}

	@Override
	public String getUrl(String title) {
		// TODO Auto-generated method stub
		String url=null;
		try {
			//http://weibo.cn/search/mblog/?keyword=%E4%BA%8C%E8%83%8E%E6%94%BF%E7%AD%96&sort=hot&vt=4
			//http://weibo.cn/search/mblog?hideSearchFrame=&keyword=%E4%BA%8C%E8%83%8E%E6%94%BF%E7%AD%96&sort=hot&page=2&vt=4
			 url ="http://weibo.cn/search/mblog?hideSearchFrame=&keyword="+URLEncoder.encode(title, "utf-8")+
					 "&sort=hot&page=" + 1 + "&vt=4";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	@Override
	public String getUrl(String title, int page) {
		// TODO Auto-generated method stub
		String url=null;
		if(this.cookie==null){
			try {
				 cookie = WeiboCN.getSinaCookie("415876780@qq.com", "yanjie123");
				//cookie = WeiboCN.getSinaCookie(this.getName(), this.getPassword());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			//http://weibo.cn/search/mblog/?keyword=%E4%BA%8C%E8%83%8E%E6%94%BF%E7%AD%96&sort=hot&vt=4
			//http://weibo.cn/search/mblog?hideSearchFrame=&keyword=%E4%BA%8C%E8%83%8E%E6%94%BF%E7%AD%96&sort=hot&page=2&vt=4
			 url ="http://weibo.cn/search/mblog?hideSearchFrame=&keyword="+URLEncoder.encode(title, "utf-8")+
					 "&sort=hot&page=" + page + "&vt=4";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

}

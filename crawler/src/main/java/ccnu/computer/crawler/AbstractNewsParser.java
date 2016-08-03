package ccnu.computer.crawler;

import java.io.IOException;
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

import ccnu.computer.dao.TextDao;

public abstract class AbstractNewsParser implements Parser {
    @Resource
    public TextDao textDao ;

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

	/*
	 * 获取一个网页上的多个链接
	 * */
	public Set<String> getItemUrl(String url,String regex,String divclassname) {
		// TODO Auto-generated method stub
		Set<String> urls= new HashSet<String>();
		Document doc = getDocs(url);
		Elements links=doc.select(divclassname);
		Elements a=links.select("a");
		for (Element link : a) {
			String myurl = link.attr("href");
			if (Pattern.matches(regex,myurl)) {
				urls.add(myurl);
			}
		}
		return urls;
	}

	


	
	public int getUrlsize(String url,String divclassname) {
		Document doc = getDocs(url);
		//Elements page = doc.select("div.l_v2");
		Elements page = doc.select(divclassname);
		Pattern pattern = Pattern.compile("[[0-9]*,]*[0-9]*");
		Matcher m = pattern.matcher(page.text());
		int pagesize = 0;
		while (m.find()) {
			if (!"".equals(m.group())) {
				String number = m.group();
				if (number.contains(",")) {
					String[] str = number.split(",");
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < str.length; i++) {
						sb = sb.append(str[i]);
					}
					// this.setCount(Integer.parseInt(sb.toString()));
					System.out.println("总共找到：" + sb.toString() + "篇相关新闻");
					pagesize = Integer.parseInt(sb.toString()) / 20;
					break;
				} else {
					System.out.println("总共找到：" + m.group() + "篇相关新闻");
					// this.setCount(Integer.parseInt(number));
					pagesize = Integer.parseInt(m.group()) / 20;
					break;
				}
			}
		}
		return pagesize + 1;
	}
	public String getUrl(String title, String url1, String url2) {
		// TODO Auto-generated method stub
		String url = null;
		try {
			url = url1+ URLEncoder.encode(title, "utf-8")+url2;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	public String getUrl(String title, int page, String url1, String url2, String url3) {
		// TODO Auto-generated method stub
		return getUrl(title,url1,url2)+page+url2;
	}


}
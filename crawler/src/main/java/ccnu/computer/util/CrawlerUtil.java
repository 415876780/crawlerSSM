package ccnu.computer.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerUtil {
	public static String getTime(Document doc, List<String> listTag) {
		// TODO Auto-generated method stub
		Elements timeTag = null;
		String time = null;
		for (String tag : listTag) {
			timeTag = doc.select(tag);
			if (!timeTag.isEmpty())
				break;
		}
		if (!timeTag.isEmpty()) {
			Pattern pattern = Pattern
					.compile("[0-9]{4}[年|\\-|/][0-9]{1,2}[月|\\-|/][0-9]{1,2}");
			Matcher m = pattern.matcher(timeTag.text());
			while (m.find()) {
				if (!"".equals(m.group())) {
					time = m.group();
					break;
				}
			}
		} else {//若找不到，则全局匹配
			Pattern pattern = Pattern
					.compile("[0-9]{4}[年|\\-|/][0-9]{1,2}[月|\\-|/][0-9]{1,2}");
			Matcher m = pattern.matcher(doc.html());
			while (m.find()) {
				if (!"".equals(m.group())) {
					time = m.group();
					break;
				}
			}
		}
		return time;
	}
	
	public static String getContent(Document doc, List<String> listTag) {
		// TODO Auto-generated method stub
		Elements p=null;
		for(String tag:listTag){
			p= doc.select(tag);
			if(!p.isEmpty()) break;
		}
		StringBuffer sb=new StringBuffer();
		if(!p.isEmpty()){
			for (Element el : p) {
					sb.append(el.text());
				}
		}
		return sb.toString();
	}
	
	
	public static String getTitle(Document doc, String tag) {
		// TODO Auto-generated method stub
		String result=null;
		Elements title = doc.select(tag);	
		if(title==null) return "";
		
		return title.first().select("h1").text();
	}
}

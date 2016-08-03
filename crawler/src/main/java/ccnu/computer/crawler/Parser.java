package ccnu.computer.crawler;

import java.net.URL;
import java.util.Set;

import org.jsoup.nodes.Document;

public interface Parser {
	/*
	 * 通过关键词得到url
	 * @param url1
	 * 		搜索url的前半部分
	 * @param url2
	 * 		搜索url的后半部分
	 * */
    public String getUrl(String title);
  
    /*
     * 用来获取搜索结果的总页面数
     * @param 
     * */
    public int getUrlsize(String url);
    /*
     * 通过页面数来生成url,url分别由url1,url2,url3三部分以及page组成
     * 
     * */
    
    public String getUrl(String title,int page);
    /*
     * 用来获取搜索界面上新闻链接
     * @param url
     * 		url 为新闻链接的网址
     * @param regex
     * 		regex 为正则匹配的网址
     * */
    public Set<String> getItemUrl(String url);
    
	
	public Document getDocs(String url);
	/*
	 * 获取新闻的内容
	 * */
	public void parse( Document doc,String baseTag,String topic);
}
package ccnu.computer.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ccnu.computer.crawler.Fetch;
import ccnu.computer.model.Poster;
import ccnu.computer.model.Text;
import ccnu.computer.model.User;
import ccnu.computer.service.ITextService;
import ccnu.computer.service.IWeiBoService;

@Controller
@RequestMapping("/text")
public class TextController {
	private ITextService textService;
	private IWeiBoService weiboService;
	private Fetch fetch;
	
	
	public IWeiBoService getWeiboService() {
		return weiboService;
	}
	@Resource
	public void setWeiboService(IWeiBoService weiboService) {
		this.weiboService = weiboService;
	}
	public Fetch getFetch() {
		return fetch;
	}
	@Resource
	public void setFetch(Fetch fetch) {
		this.fetch = fetch;
	}
	public ITextService getTextService() {
		return textService;
	}
	@Resource
	public void setTextService(ITextService textService) {
		this.textService = textService;
	}
	
	@RequestMapping(value={"/texts","/"},method=RequestMethod.GET)
	public String list(Model model){
		model.addAttribute("pager", textService.find());
		
		return "text/news";
	}
	@RequestMapping(value={"/weibos","/"},method=RequestMethod.GET)
	public String weibolist(Model model){
		model.addAttribute("pager", weiboService.find());
		
		return "text/weibos";
	}
	
	@RequestMapping(value={"/alltexts","/"},method=RequestMethod.GET)
	public String listAllTexts(Model model){
		model.addAttribute("pager", textService.finds());
		
		return "text/allnews";
	}
	@RequestMapping(value={"/allweibos","/"},method=RequestMethod.GET)
	public String listAllWeiBos(Model model){
		model.addAttribute("pager", weiboService.finds());
		
		return "text/allweibos";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.GET) 
	public String add(Model model){
		model.addAttribute(new Text());
		return "text/add";
	}
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(String title,String select,Model model){
		fetch.setT(title);
		fetch.setSelect(select);
		new Thread(fetch).start();
		//return "redirect:/text/texts";
		return "text/add";
	}

	@RequestMapping(value="/{id}/show",method=RequestMethod.GET)
	public String show(@PathVariable int id ,Model model){
		model.addAttribute("text",textService.load(id));
		return "text/show";
	}
	

	
	@RequestMapping(value="/{id}/show",method=RequestMethod.POST)
	public String show(@PathVariable int id,@Validated Text text,BindingResult br,
			Model model,HttpServletRequest request, HttpServletResponse response){
		
		System.out.println(text.getIsRelated());
		//User u = textService.login(username);
		Text t = textService.load(id);
		t.setIsLabel("已标记");
		t.setLabelname((((User)request.getSession().getAttribute("loginUser")).getNickname()));
		t.setIsRelated(text.getIsRelated());
		//model.addAttribute("loginUser",t);
		textService.update(t);
		//这里的redirect：的值是和@Requestmapping中的value想对应
		return "redirect:/text/texts";
	}
	
	@RequestMapping(value="/{id}/weibos",method=RequestMethod.GET)
	public String weibos(@PathVariable int id ,Model model){
		model.addAttribute("text",weiboService.load(id));
		return "text/show";
	}
	
	@RequestMapping(value="/{id}/weibos",method=RequestMethod.POST)
	public String weibos(@PathVariable int id,@Validated Text text,BindingResult br,Model model,
			HttpServletRequest request, HttpServletResponse response){
		
		System.out.println(text.getIsRelated());
		//User u = textService.login(username);
		Poster t = weiboService.load(id);
		t.setIsLabel("已标记");
		t.setLabelname((((User)request.getSession().getAttribute("loginUser")).getNickname()));
		t.setIsRelated(text.getIsRelated());
		//model.addAttribute("loginUser",t);
		weiboService.update(t);
		
		return "redirect:/text/weibos";
	}
	
	@RequestMapping(value="/{id}/update",method=RequestMethod.GET)
	public String update(@PathVariable int id,Model model){
		model.addAttribute(textService.load(id));
		return "text/update";
	}
	
	
	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable int id){
		textService.delete(id);
		return "redirect:/text/texts";
	}
}

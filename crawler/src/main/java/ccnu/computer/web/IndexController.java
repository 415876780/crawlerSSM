package ccnu.computer.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.helpers.LogLog;
import org.eclipse.jetty.util.log.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import ccnu.computer.model.User;
import ccnu.computer.service.IUserService;
@Controller
@SessionAttributes("loginUser")
public class IndexController {
	
	private IUserService userService;
	
	public IUserService getUserService() {
		return userService;
	}



	@Resource
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(){
		return "login";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(String username,String password,Model model){
		Log.info(username+password);
		User u = userService.login(username, password);
		model.addAttribute("loginUser",u);
		return "redirect:/text/texts";
	}
	@RequestMapping("/logout")
	public String logout(Model model,HttpSession session){
		model.asMap().remove("loginUser");
		session.invalidate();
		return "redirect:/login";
	}
}

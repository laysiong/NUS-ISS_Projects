package nus.iss.team07.laps.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import nus.iss.team07.laps.interfacemethods.EmployeeInterface;
import nus.iss.team07.laps.model.Employee;
import nus.iss.team07.laps.model.UserSession;

@Controller
public class CommonController {
   
	@Autowired
	private EmployeeInterface employeeService;
	
		@GetMapping("/")
	    public String index(Model model) {
		  model.addAttribute("user", new Employee());
	      return "index";
	    }

	    @PostMapping("/login/authenticate")
	    public String authenticate(@ModelAttribute("user") @Validated(Employee.LoginValidationGroup.class) Employee user, BindingResult bindingResult, Model model, HttpSession session) {
	        if (bindingResult.hasErrors()) {
        	   System.out.println("There are errors in the form:");
               bindingResult.getAllErrors().forEach(error -> {
                   System.out.println(error.getObjectName() + ": " + error.getDefaultMessage());
               });
               
	            model.addAttribute("user", user);
	            return "index";
	        }

	        Employee e_user = employeeService.authenicate(user.getUsername(), user.getPassword());


	        if (e_user == null) {
	            model.addAttribute("loginMessage", "Incorrect username/password");
	            return "index";
	        }

	        
	        // Login - Clear
	        UserSession userSession = new UserSession();
	        userSession.setEmployee(e_user);
	        userSession.setRoles(e_user.getRole().getType());

	        session.setAttribute("usession", userSession);
	        //System.out.println("User session set: " + session.getAttribute("usession"));
	        //System.out.println("Roles set in session: " + userSession.getEmployee().getId());
	        
	        return "redirect:/mainmenu";
	    }
   
	   @RequestMapping(value = "/logout")
	   public String logout(HttpSession session) {
	     session.invalidate();
	     return "redirect:/";
	   }
	   
	   @RequestMapping("/unauthorized")
	    public String unauthorized() {
	        return "unauthorized"; 
	    }
	  
	   
}

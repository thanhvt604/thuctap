package com.globits.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class StudentExersiceController {
	@Autowired
    MyFirstApiService myFisrtApiService;
	  
    @RequestMapping(method = RequestMethod.GET ,value="/myfisrtapi")
    @ResponseBody
    public String MyFirstApi() 
    {
    	return "My First API";
    }
    
    @RequestMapping(method = RequestMethod.GET ,value="/myFisrtApi")
    @ResponseBody
    public String MyFirstApiService() 
    {
    	return myFisrtApiService.returnString();
    }
    
    @RequestMapping(method = RequestMethod.GET ,value="/responseDTO/{code}/{name}/{age}")
    @ResponseBody
    public String responseDTO(@PathVariable("code") String code,@PathVariable("name") String name,@PathVariable("age") int age)
    {
    	DTO dto=new DTO(code,name,age);
    	return dto.toString();
    }
}
	
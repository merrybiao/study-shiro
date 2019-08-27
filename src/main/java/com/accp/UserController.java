package com.accp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

   /**
    * 测试thymeleaf
    * @param model
    * @return
    */
   @RequestMapping("/testThymeleaf")
   public String testThymeleaf(Model model){
       //将数据存入model
       model.addAttribute("name","路西法");
       //返回test.html
       return "test";
   }
}

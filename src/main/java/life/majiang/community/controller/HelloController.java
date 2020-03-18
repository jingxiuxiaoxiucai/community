package life.majiang.community.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    @GetMapping("hello")
    public  String hello(@RequestParam(name = "name") String name, Model model){
        model.addAttribute("name",name);
       return "hello";
    }
}

package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    QuestionService questionService;


    /**
     * 前台 index 传来三个参数
     * search 可以为空
     *
     * @param request
     * @param model
     * @param page
     * @param size
     * @param search
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request
            , Model model
            , @RequestParam(name = "page", defaultValue = "1") Integer page
            , @RequestParam(name = "size", defaultValue = "2") Integer size
            , @RequestParam(name = "search", required = false) String search
    ) {

        PaginationDTO paginationDTO = questionService.list(search, page, size);
        model.addAttribute("pagination", paginationDTO);
        model.addAttribute("search", search);
        return "index";
    }
}

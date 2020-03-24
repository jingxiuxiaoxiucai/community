package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.service.CommentService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
@Autowired
private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")long id, Model model){
     List<CommentDTO> commentDTOList= commentService.listByQuestion( id);
     //累计增加阅读数
        questionService.incView(id);
      QuestionDTO questionDTO= questionService.getById(id);

      model.addAttribute("questionDTO",questionDTO);
      model.addAttribute("commentDTOList",commentDTOList);

        return "question";
    }
}

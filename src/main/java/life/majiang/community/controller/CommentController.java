package life.majiang.community.controller;

import life.majiang.community.dto.CommentCreateDTO;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.model.Comment;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

        @Autowired
      private CommentService commentService;

        @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO
    , HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user ==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LONG);
        }

        Comment comment = new Comment();
            comment.setParentId(commentCreateDTO.getParentId());
            comment.setContent(commentCreateDTO.getContent());
            comment.setType(commentCreateDTO.getType());
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(System.currentTimeMillis());
            comment.setCommentator(user.getId());
            comment.setLikeCount(0l);
         commentService.insert(comment);
            Map<Object,Object> objectObjectMap=new HashMap<>();
            objectObjectMap.put("message","成功");
        return  ResultDTO.okOf();
    }
}

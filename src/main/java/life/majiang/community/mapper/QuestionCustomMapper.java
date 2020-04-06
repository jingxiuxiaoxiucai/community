package life.majiang.community.mapper;

import life.majiang.community.dto.QuestionQueryDTO;
import life.majiang.community.model.Question;

import java.util.List;


public interface QuestionCustomMapper {

    int incView(Question question);
    int incCommentCount(Question question);
    List<Question> selectRegexpTag(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
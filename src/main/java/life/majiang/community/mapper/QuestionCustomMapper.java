package life.majiang.community.mapper;

import life.majiang.community.model.Question;


public interface QuestionCustomMapper {

    int incView(Question question);
    int incCommentCount(Question question);
}
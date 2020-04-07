package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.dto.QuestionQueryDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionCustomMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import life.majiang.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionCustomMapper questionCustomMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(String search, Integer page, Integer size) {

        if (StringUtils.isNotBlank(search)) {//不为空
            String[] tagSplit = StringUtils.split(search, " ");
            search = Arrays.stream(tagSplit).collect(Collectors.joining("|"));

        }

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalpage;

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);

        Integer totalcount = questionCustomMapper.countBySearch(questionQueryDTO);

        if (totalcount % size == 0) {
            totalpage = totalcount / size;
        } else {
            totalpage = totalcount / size + 1;
        }

        if (page > totalpage) {
            page = totalpage;
        }
        if (page < 1) {
            page = 1;
        }

        paginationDTO.setpaginationDTO(totalpage, page, size);

        Integer offset = size * (paginationDTO.getPage() - 1);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        //  List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionCustomMapper.selectBySearch(questionQueryDTO);

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalcount = (int) questionMapper.countByExample(questionExample);
        Integer totalpage;
        if (totalcount % size == 0) {
            totalpage = totalcount / size;
        } else {
            totalpage = totalcount / size + 1;
        }

        if (page > totalpage) {
            page = totalpage;
        }
        if (page < 1) {
            page = 1;
        }
        paginationDTO.setpaginationDTO(totalpage, page, size);
        Integer offset = size * (paginationDTO.getPage() - 1);


        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        } else {
            // 更新

            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

        }
    }

    public void incView(long id) {

        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionCustomMapper.incView(question);
    }

    public List<QuestionDTO> selectRegexpTag(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())) {
            return new ArrayList<>();
        }

        String[] tagSplit = StringUtils.split(questionDTO.getTag(), ",");
        String regexptag = Arrays.stream(tagSplit).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexptag);
        List<Question> questionList = questionCustomMapper.selectRegexpTag(question);
        List<QuestionDTO> questionDTOList = questionList.stream().map(q -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());

        return questionDTOList;
    }
}

package com.mysite.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.mysite.sbb.DataNotFoundException;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository qR;

    public List<Question> getList(){
        return qR.findAll();
    }

    public Question getQuestion(Integer id){
        Optional<Question> question = this.qR.findById(id);
        if(question.isPresent()){
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        this.qR.save(q);
    }
}

package com.mysite.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.mysite.sbb.DataNotFoundException;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository qR;

    public List<Question> getList(){
        return this.qR.findAll();
    }

    public Question getQuestion(Integer id){
        Optional<Question> question = this.qR.findById(id);
        if(question.isPresent()){
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }
}

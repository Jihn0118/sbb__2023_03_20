package com.mysite.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService qS;

    @GetMapping("/question/list")
    public String list(Model model) {
        List<Question> questionList = this.qS.getList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }
}
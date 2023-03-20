package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository qR;

	@Autowired
	private AnswerRepository aR;

	@Test
	void testJpa() {
		// 첫번째 질문 저장
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.qR.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문임다");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.qR.save(q2);
	}

	@Test
	void testJpaFindAll(){
		List<Question> all = this.qR.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Test
	void testJpaFindByID(){
		Optional<Question> oq = this.qR.findById(1);
		if(oq.isPresent()){
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}

	@Test
	void testJpaFindBySubject(){
		Question q = this.qR.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());
	}

	@Test
	void testJpafindBySubjectAndContent(){
		Question q = this.qR.findBySubjectAndContent(
				"sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다."
		);
		assertEquals(1, q.getId());
	}

	@Test
	void testJpafindBySubjectLike(){
		List<Question> qList = this.qR.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Test
	void testJpaModify(){
		Optional<Question> oq = this.qR.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.qR.save(q);
	}

	@Test
	void testJpaDelete(){
		assertEquals(2, this.qR.count());
		Optional<Question> oq = this.qR.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.qR.delete(q);
		assertEquals(1, this.qR.count());
	}

	@Test
	void testJpaAnswerInsert(){
		Optional<Question> oq = this.qR.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);
		a.setCreateDate(LocalDateTime.now());
		this.aR.save(a);
	}

	// Transactional를 사용하면 메서드가 종료될 때까지 DB세션이 유지된다.
	@Transactional
	@Test
	void testJpaQNA(){
		Optional<Question> oq = this.qR.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}
}

package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.user.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
	private UserService uS;

	@Autowired
	private QuestionService qS;

	@Autowired
	private QuestionRepository qR;

	@Autowired
	private AnswerRepository aR;

	@Autowired
	private UserRepository uR;

	@BeforeEach
		// 아래 메서드는 각 테스트케이스가 실행되기 전에 실행된다.
	void beforeEach() {
		// 회원 정보 삭제
		uR.deleteAll();
		uR.clearAutoIncrement();

		// 모든 데이터 삭제
		aR.deleteAll();
		aR.clearAutoIncrement();

		// 모든 데이터 삭제
		qR.deleteAll();

		// 흔적삭제(다음번 INSERT 때 id가 1번으로 설정되도록)
		qR.clearAutoIncrement();

		// 회원 2명 생성
		uS.create("user1", "user1@test.com", "1234");
		uS.create("user2", "user2@test.com", "1234");

		// 질문 1개 생성
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		qR.save(q1);  // 첫번째 질문 저장

		// 질문 1개 생성
		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		qR.save(q2);  // 두번째 질문 저장

		// 답변 1개 생성
		Answer a1 = new Answer();
		a1.setContent("네 자동으로 생성됩니다.");
		a1.setCreateDate(LocalDateTime.now());
		q2.addAnswer(a1);
		aR.save(a1);
	}

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

	@Test
	void testJpa300Case(){
		for(int i = 1; i <= 300; i++){
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.qS.create(subject, content);
		}
	}
}

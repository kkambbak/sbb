package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @BeforeEach // 각 테스트케이스가 실행되기 전에 실행된다.
    void beforeEach(){
        //모든 데이터 삭제
        questionRepository.deleteAll();
        //흔적삭제 다음 insert때 id가 1번 되도록
        questionRepository.clearAutoIncrement();
        answerRepository.clearAutoIncrement();

        //초기 데이터 설정
        // TODO: user 부분 변경
        Question q1 = questionService.create("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.", null);
        questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = questionService.create("스프링부트 모델 질문입니다.", "id는 자동으로 생성되나요?", null);
        questionRepository.save(q2);// 두번째 질문 저장

        //답변저장
        q2.addAnswer(answerService.create(q2, "네 자동으로 생성됩니다."));
    }


    @Test
    @DisplayName("test save")
    void t1(){
        // 질문 1개 생성
        questionService.create("세계에서 가장 부유한 국가가 어디인가요?", "알고 싶습니다.", null);

        assertEquals("세계에서 가장 부유한 국가가 어디인가요?", questionRepository.findById(3).get().getSubject());
    }

    @Test
    @DisplayName("test findAll")
    void t2() {
        List<Question> all = this.questionRepository.findAll();
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("test findById")
    void t3() {
        Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()) {
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    @Test
    @DisplayName("test findBySubject")
    void t4() {
        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("test findBySubjectAndContent")
    void t5() {
        Question q = this.questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        assertEquals(1, q.getId());
    }

    @Test
    @DisplayName("test findBySubjectLike sbb%")
    void t6() {
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @DisplayName("test modify")
    void t7() {
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        Question modifiedQ = q.toBuilder().subject("수정된 제목").build();
        questionRepository.save(modifiedQ);
    }

    @Test
    @DisplayName("test delete")
    void t8() {
        assertEquals(2, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }

    @Test
    @DisplayName("test saveAnswer")
    void t9() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        answerService.create(q, "네 자동으로 생성됩니다.");
        //assertThat
    }

    @Test
    @DisplayName("test findAnswer")
    void t10(){
        Optional<Answer> oa = this.answerRepository.findById(1);
        assertTrue(oa.isPresent());
        Answer a = oa.get();
        assertEquals(2, a.getQuestion().getId());
    }

    @Transactional
    //@Rollback(false)
    @Test
    @DisplayName("질문에 달린 답변 찾기")
    void t11() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }

    @Test
    @DisplayName("300개 테스트 데이터 넣기")
    void t12() {
        for (int i = 1; i <= 300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.questionService.create(subject, content, null);
        }
    }
}

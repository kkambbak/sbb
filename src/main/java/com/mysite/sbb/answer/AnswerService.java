package com.mysite.sbb.answer;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {
	private final AnswerRepository answerRepository;

	public Answer create(Question question, String content) {
		Answer answer = Answer.builder()
			.content(content)
			.createDate(LocalDateTime.now())
			.modifyDate(LocalDateTime.now())
			.question(question)
			.build();
		return answerRepository.save(answer);
	}

	public Answer create(Question question, String content, SiteUser author) {
        Answer answer = Answer.builder()
            .content(content)
            .createDate(LocalDateTime.now())
            .modifyDate(LocalDateTime.now())
            .question(question)
            .author(author)
            .build();

        return answerRepository.save(answer);
	}

	public Answer getAnswer(Integer id) {
		Optional<Answer> answer = answerRepository.findById(id);
		if (answer.isEmpty())
			throw new DataNotFoundException("answer not found");
		return answer.get();
	}

	public Answer modify(Answer answer, String content) {
        //검증 필요?
        Answer modifiedAnswer = answer.toBuilder().content(content).build();
        return answerRepository.save(modifiedAnswer);
	}

	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}

	public void vote(Answer answer, SiteUser siteUser) {
		answer.getVoter().add(siteUser);
		answerRepository.save(answer);
	}
}

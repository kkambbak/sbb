package com.mysite.sbb.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer,Integer> {

    @Query(value = "ALTER TABLE answer AUTO_INCREMENT = 1", nativeQuery = true )
    void clearAutoIncrement();
}

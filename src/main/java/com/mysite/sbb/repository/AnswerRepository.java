package com.mysite.sbb.repository;

import com.mysite.sbb.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer,Integer> {

    @Query(value = "ALTER TABLE answer AUTO_INCREMENT = 1", nativeQuery = true )
    void clearAutoIncrement();
}

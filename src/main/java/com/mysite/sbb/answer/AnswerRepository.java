package com.mysite.sbb.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    @Transactional
    @Modifying
    // @Modifying: 만약 아래 쿼리가 SELECT가 아니라면 이 어노테이션을 붙여야한다.
    @Query(value = "ALTER TABLE answer AUTO_INCREMENT = 1", nativeQuery = true)
        // nativeQuery = true 여야 MySQL 쿼리문법 사용 가능
    void clearAutoIncrement();
}

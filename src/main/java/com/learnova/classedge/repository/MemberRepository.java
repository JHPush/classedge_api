package com.learnova.classedge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnova.classedge.domain.Member;


@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    // id로 회원 조회
    Optional<Member> findById(String id);

}

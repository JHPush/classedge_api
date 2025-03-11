package com.learnova.classedge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnova.classedge.domain.Member;


@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

}

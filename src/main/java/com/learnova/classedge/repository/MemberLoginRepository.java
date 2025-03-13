package com.learnova.classedge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learnova.classedge.domain.Member;

public interface MemberLoginRepository extends JpaRepository<Member, String> { // <Entity, PK>
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Member getMemberByEmail(@Param("email") String email);

    @Query("SELECT m FROM Member m WHERE m.id = :id")
    Member getMemberById(@Param("id") String id);
}

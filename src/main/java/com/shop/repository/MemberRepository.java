package com.shop.repository;

import com.shop.constant.Role;
import com.shop.dto.MemberDto;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    @Query("select m.id from Member m " +
            "where m.email = :email"
    )
    Long findIdByEmail(@Param("email") String email);

    List<MemberDto> findAllByRole(Role role);
}
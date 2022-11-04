package com.shop.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDto {

    private Long id;

    private String name;

    private String email;

    private Role role;

    @QueryProjection
    public MemberDto(Long id, String name, String email, Role role){

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}

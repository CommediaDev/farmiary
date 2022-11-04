package com.shop.service;

import com.shop.constant.Role;
import com.shop.dto.MemberDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    
    //회원 정보창 로그인 비밀번호 확인
    public boolean checkPassword(String realPassword, String checkPassword) {
        boolean matches = passwordEncoder.matches(checkPassword, realPassword);
        return matches;
    }
    //회원 정보 수정
    public void UpdateMember(Member member){
        Long memberId = memberRepository.findIdByEmail(member.getEmail());
        member.setId(memberId);
        System.out.println(member.toString());
        System.out.println("======================================================================");
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    //회원 권한 설정
    public void grantRole(String email, Role role){
        Member member = memberRepository.findByEmail(email);

        System.out.println("======================================");
        System.out.println(member.toString());
        System.out.println("======================================");

        member.setRole(role);

        memberRepository.save(member);

        System.out.println("======================================");
        System.out.println(member.toString());
        System.out.println("======================================");
    }

    //회원 리스트 Dto 가져오기
    public List<MemberDto> getMemberDtoList() {
        List<MemberDto> memberDtoList = memberRepository.findAllByRole(Role.CANDIDATE);

        return  memberDtoList;
    }
}
package com.shop.controller;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import com.shop.repository.MemberRepository;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }


    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLoginForm";
    }

    //회원 정보 수정 창을 접근하기 위해 다시한번 로그인
    @GetMapping(value = "/infoLogin")
    public String infoLogin(){
        return "/member/memberModifyLoginForm";
    }
    @GetMapping(value = "/infoLogin/error")
    public String infoLoginError(Model model){
        model.addAttribute("loginErrorMsg", "비밀번호를 확인해주세요");
        return "member/memberModifyLoginForm";
    }
    
    //회원 정보 수정 창 접근 위한 로그인 처리
    @PostMapping(value = "/infoLogin")
    public String infoLogin(MemberFormDto memberFormDto, Principal principal){
        String checkPassword = memberFormDto.getPassword();
        Member member = memberRepository.findByEmail(principal.getName());
        String realPassword = member.getPassword();

        if(memberService.checkPassword(realPassword, checkPassword)){
            return "redirect:/members/update";
        }else {
            return "redirect:/members/infoLogin/error";
        }
    }

    //회원 정보 수정 페이지
    @GetMapping(value = "/update")
    public String modifyMember(Model model, Principal principal) {

        model.addAttribute("memberFormDto", new MemberFormDto());

        return "member/memberModifyForm";
    }
    
    //회원 정보 수정 처리
    @PostMapping(value = "/update")
    public String modifyMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model, Principal principal){

        if(bindingResult.hasErrors()){
            return "member/memberModifyForm";
        }

        try {

            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.UpdateMember(member);
        } catch (IllegalStateException e){
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberModifyForm";
        }

        return "redirect:/";
    }

    //판매자 신청 페이지
    @GetMapping(value = "/request")
    public String requestSeller(){
        return "member/sellerRequestForm";
    }

    //판매자 신청 처리
    @PostMapping(value = "/request")
    public String RequestSeller(Principal principal){
        memberService.grantRole(principal.getName(), Role.CANDIDATE);
        return "redirect:/members/logout";
    }

}
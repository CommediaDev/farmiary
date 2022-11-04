package com.shop.controller;

import com.shop.constant.Role;
import com.shop.dto.MemberDto;
import com.shop.repository.MemberRepository;
import com.shop.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    //판매자 요청 수락 페이지
    @GetMapping("/admin/requestPermit")
    public String permitPage(Model model){
        List<MemberDto> memberDtoList = memberRepository.findAllByRole(Role.CANDIDATE);
        model.addAttribute("memberDtoList", memberDtoList);

        return "member/candidateList";
    }

    //판매자 요청 수락
    @PostMapping("/admin/requestPermit")
    public String permitProc(MemberDto memberdto) {
        String email = memberdto.getEmail();
        System.out.println(email);
        System.out.println("=============================");
        memberService.grantRole(email, Role.SELLER);

        return "redirect:/myPage";
    }
}

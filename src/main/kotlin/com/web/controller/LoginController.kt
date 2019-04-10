package com.web.controller

import com.web.annotation.SocialUser
import com.web.domain.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {
    @GetMapping("/login")
    fun login() = "login"

    @GetMapping(value = ["/{facebook|google|kakao}/complete"])      // 허용되는 URL 매핑은 3가지로 제한함
    fun loginComplete(@SocialUser user: User): String {
        return "redirect:/board/list"
    }
}
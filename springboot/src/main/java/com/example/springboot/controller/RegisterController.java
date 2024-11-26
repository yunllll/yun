package com.example.springboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.springboot.service.EmailService;

@RestController
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private EmailService EmailService;

    @PostMapping("/register")
    public Response sendCode(@RequestParam String to, HttpSession httpSession) {
        String randomCode = EmailService.email(to);
        httpSession.setAttribute("code", randomCode);
        return new Response("验证码已发送到指定邮箱", randomCode);
    }

    @PostMapping("/checkCode")
    public Response checkCode(@RequestParam String code, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return new Response("验证码未发送", null);
        }
        String storedCode = (String) session.getAttribute("code");
        if (code.equals(storedCode)) {
            return new Response("验证码正确", null);
        }
        return new Response("验证码错误", null);
    }

    public static class Response {
        private String message;
        private String data;

        public Response(String message, String data) {
            this.message = message;
            this.data = data;
        }

        // Getters and setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
package com.example.examspring.controller;

import com.example.examspring.model.Members;
import com.example.examspring.model.Role;
import com.example.examspring.repository.MemberRepository;
import com.example.examspring.repository.RoleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public LoginController(MemberRepository memberRepository, RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("member") Members member, HttpSession session, Model model) {
        String userId = member.getUserId();
        String password = member.getPassword();

        // Giá trị userId và password trực tiếp
        String directUserId = "admin";
        String directPassword = "admin123";

        // Check if the user exists and the password is correct
        Optional<Members> optionalMember = memberRepository.findByUserId(userId);
        if (optionalMember.isPresent()) {
            Members authenticatedMember = optionalMember.get();
            if (authenticatedMember.getPassword().equals(password)) {
                // Get roles of the member
                List<Role> roles = roleRepository.findByUserId(userId);
                if (!roles.isEmpty()) {
                    Role role = roles.get(0); // Assuming one member has one role for simplicity
                    // Set user info in session
                    session.setAttribute("userId", userId);
                    session.setAttribute("role", role.getRole());
                    // Redirect based on role
                    if (role.getRole().equals("Admin")) {
                        return "redirect:/vehicles/list";
                    } else if (role.getRole().equals("Sale")) {
                        return "redirect:/vehicles/form";
                    }
                }
            }
        }

        // Kiểm tra giá trị trực tiếp
        if (userId.equals(directUserId) && password.equals(directPassword)) {
            // Set user info in session
            session.setAttribute("userId", directUserId);
            session.setAttribute("role", "Admin"); // Ví dụ: Set vai trò "Admin"
            return "redirect:/vehicles/list"; // Redirect to Admin page
        }

        // Authentication failed
        model.addAttribute("error", "Invalid username or password");
        return "index";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate session
        session.invalidate();
        return "redirect:/";
    }
}

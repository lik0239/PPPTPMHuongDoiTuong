package com.example.courseReg.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping("/login")
  public String login(Authentication authentication) {
      if (authentication != null
              && authentication.isAuthenticated()
              && !(authentication instanceof AnonymousAuthenticationToken)) {
          return "redirect:/";
      }
      return "login";
  }

  @GetMapping("/register")
  public String showRegister(Model model) {
    model.addAttribute("form", new AuthService.RegisterForm());
    return "register";
  }

  @PostMapping("/register")
  public String doRegister(@ModelAttribute("form") @Valid AuthService.RegisterForm form,
                          BindingResult result, Model model) {
    if (result.hasErrors()) return "register";
    try {
      authService.register(form);
    } catch (IllegalArgumentException ex) {
      result.rejectValue("username","exists", ex.getMessage());
      return "register";
    } catch (Exception ex) {
      result.reject("global", "Có lỗi khi tạo tài khoản");
      return "register";
    }
    return "redirect:/login?registered";
  }
}

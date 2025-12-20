package app.Controller;

import app.Database.User;
import app.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 1️⃣ HOME PAGE
    @GetMapping("/")
    public String home() {
        return "home";   // home.html
    }

    // 2️⃣ SHOW LOGIN PAGE
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // login.html
    }

    // 3️⃣ PROCESS LOGIN
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String userName,
            @RequestParam String password
    ) {
        userService.loginUser(userName, password);
        return "redirect:/"; // or dashboard
    }

    // 4️⃣ SHOW REGISTER PAGE
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // register.html
    }

    // 5️⃣ PROCESS REGISTER
    @PostMapping("/register")
    public String processRegister(
            @RequestParam String userName,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam(required = false) Long phoneNumber
    ) {
        User user = new User(
                userName,
                password,
                firstName,
                lastName,
                email,
                phoneNumber
        );

        userService.registerUser(user);
        return "redirect:/login";
    }
}

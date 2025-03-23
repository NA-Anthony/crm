package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Map<String, Object> response = new HashMap<>();

        // Vérifier les informations de login
        User user = authService.checkLogin(username, password);

        if (user != null) {
            // Login réussi
            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("user", user); // Retourner les informations de l'utilisateur
            return ResponseEntity.ok(response);
        } else {
            // Login échoué
            response.put("status", "error");
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(401).body(response); // Code HTTP 401 Unauthorized
        }
    }
}
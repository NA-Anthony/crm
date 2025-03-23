package site.easy.to.build.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.repository.UserRepository;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User checkLogin(String username, String password) {
        List<User> users = userRepository.findByUsername(username);
        if (!users.isEmpty()) {
            User user = users.get(0);
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
package app.Service;

import app.Database.User;
import app.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public void registerUser(
            User user
    ) {
        if(userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByemail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        userRepository.save(user);
    }

    public void loginUser(String username, String password) {
        Optional<User> user = userRepository.findByUserName(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {

        }

    }
}

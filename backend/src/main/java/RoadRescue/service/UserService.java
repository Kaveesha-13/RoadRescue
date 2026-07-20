package RoadRescue.service;

import RoadRescue.model.User;
import RoadRescue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //  User Registration
    public User registerUser(User user) {
        
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

       
        if (user.getRole() == User.Role.ADMIN) {
            user.setStatus(User.Status.APPROVED);
        } else {
            user.setStatus(User.Status.PENDING);
        }

        // Save and return the user object
        return userRepository.save(user);
    }

    //  User Login
    public User loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
          
            if (user.getPassword().equals(password)) {
                if (user.getStatus() == User.Status.APPROVED) {
                    return user;
                } else if (user.getStatus() == User.Status.PENDING) {
                    throw new RuntimeException("Your registration request is still PENDING admin approval.");
                } else {
                    throw new RuntimeException("Your registration request was REJECTED by the admin.");
                }
            }
        }
        throw new RuntimeException("Invalid username or password!");
    }

    // Get all pending registration requests
    public List<User> getPendingUsers() {
        return userRepository.findByStatus(User.Status.PENDING);
    }

    //  Approve or Reject a user account
    public User updateUserStatus(Long userId, User.Status newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        user.setStatus(newStatus);
        return userRepository.save(user);
    }

    // Fetch all registered vehicle owners for the admin dashboard
    public List<User> getAllApprovedVehicleOwners() {
        return userRepository.findByRole(User.Role.VEHICLE_OWNER);
    }
}
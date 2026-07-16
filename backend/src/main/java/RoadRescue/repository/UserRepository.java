package RoadRescue.repository;

import RoadRescue.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find a user by their username 
    Optional<User> findByUsername(String username);
    
    // Check if a username or email is already taken during signup
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // Used by Admin to filter users by their approval status or role
    List<User> findByRole(User.Role role);
    List<User> findByStatus(User.Status status);
}
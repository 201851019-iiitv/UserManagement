package ambesh.UserManagement.repository;

import ambesh.UserManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    // all crud database methods

}
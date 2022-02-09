package ambesh.UserManagement.DAO;

import ambesh.UserManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByMobileno(String mobileNumber);

    User findByEmail(String email);

    User findByUsername(String username);
    // all crud database methods

}
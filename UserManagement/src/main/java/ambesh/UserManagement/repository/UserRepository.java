package ambesh.UserManagement.repository;

import ambesh.UserManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    //List<User> findByFirstName(String firstName);
    //    save
    //    findOne
   //    exists
   //    findAll
  //    count
  //    delete
  //    deleteAll
}
package ambesh.UserManagement.repository;

import ambesh.UserManagement.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository<P> extends CrudRepository<User,Long> {
    //List<User> findByFirstName(String firstName);
    //    save
    //    findOne
   //    exists
   //    findAll
  //    count
  //    delete
  //    deleteAll
}
package ambesh.UserManagement.services;

import ambesh.UserManagement.exception.ResourceNotFoundException;
import ambesh.UserManagement.model.User;
import ambesh.UserManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UserService {

    @Autowired
    UserRepository<User> userRepository;

    @Transactional
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Transactional
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("UserID "+id+" does not exist"));
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public boolean addUser(User user) {
       return userRepository.save(user) != null ;


    }
}
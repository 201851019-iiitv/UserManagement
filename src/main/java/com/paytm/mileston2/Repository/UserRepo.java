package com.paytm.mileston2.Repository;

import com.paytm.mileston2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByMobileNumber(String mobileNumber);
    User findByEmail(String email);
    User findByUsername(String username);

}

package com.spring.gubi.repository.users;

import com.spring.gubi.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.userid = :userid")
    Optional<User> findByUserid(@Param("userid") String userid);
}

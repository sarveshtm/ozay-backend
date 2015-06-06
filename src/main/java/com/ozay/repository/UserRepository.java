package com.ozay.repository;

import com.ozay.domain.User;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.activationKey = ?1")
    User getUserByActivationKey(String activationKey);

    @Query("select u from User u where u.activated = false and u.createdDate > ?1")
    List<User> findNotActivatedUsersByCreationDateBefore(DateTime dateTime);

    User findOneByLogin(String login);


    User findOneByEmail(String email);

}

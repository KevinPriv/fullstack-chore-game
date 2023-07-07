package com.houseworkrpg.webservice.repository;


import com.houseworkrpg.webservice.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


/**
 * Repository which allows for reading,writing,saving of entities
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Finds the first row within the [User] table with the matching username.
     * @param username you are searching for.
     * @return [User] which has been found with the matching username.
     */
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);

}

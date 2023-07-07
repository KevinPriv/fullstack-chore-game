package com.houseworkrpg.webservice.repository;

import com.houseworkrpg.webservice.entity.PlayerProfile;
import com.houseworkrpg.webservice.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository which allows for reading,writing,saving of entities
 */
public interface PlayerProfileRepository extends CrudRepository<PlayerProfile, Long> {
}

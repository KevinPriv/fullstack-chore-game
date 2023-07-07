package com.houseworkrpg.webservice.repository;

import com.houseworkrpg.webservice.entity.Group;
import com.houseworkrpg.webservice.entity.PlayerProfile;
import com.houseworkrpg.webservice.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository which allows for reading,writing,saving of entities
 */
public interface GroupRepository extends CrudRepository<Group, Long> {

    /**
     * Finds group by group code given
     * @param code group code
     * @return
     */
    Group findByCode(String code);
}

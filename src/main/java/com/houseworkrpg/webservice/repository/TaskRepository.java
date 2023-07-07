package com.houseworkrpg.webservice.repository;

import com.houseworkrpg.webservice.entity.PlayerProfile;
import com.houseworkrpg.webservice.entity.Task;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository which allows for reading,writing,saving of entities
 */
public interface TaskRepository  extends CrudRepository<Task, Long> {
}

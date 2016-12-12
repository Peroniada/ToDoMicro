package edu.peronade.repositories;

import edu.peronade.entity.ToDoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Sebastian on 12.12.2016.
 */

@RepositoryRestResource
public interface ToDoTaskRepository extends JpaRepository<ToDoTask, Long> {

}

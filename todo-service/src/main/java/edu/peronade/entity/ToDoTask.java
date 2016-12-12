package edu.peronade.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Sebastian on 12.12.2016.
 */

@Entity
public class ToDoTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String taskName;

    public ToDoTask() {
    }


    public ToDoTask(String taskName) {

        this.taskName = taskName;
    }

    public Long getId() {
        return id;
    }


    public String getTaskName() {
        return taskName;
    }

    @Override
    public String toString() {
        return "ToDoTask{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                '}';
    }

}

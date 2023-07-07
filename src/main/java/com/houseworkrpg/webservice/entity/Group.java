package com.houseworkrpg.webservice.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Household group entity
 */
@Entity
@Table(name = "housework_grp")
public class Group {

    // primary key for the group table
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // group name (does not have to be unique
    @Column(nullable = false, length = 32)
    private String groupName;

    // player profiles in the group
    @OneToMany
    @JoinColumn(name = "group_id")
    private Collection<PlayerProfile> profiles;

    // tasks made within the group
    @OneToMany
    @JoinColumn(name = "group_id")
    private Collection<Task> tasks;

    // code to join the household
    @Column(nullable = false)
    private String code;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Collection<PlayerProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Collection<PlayerProfile> profiles) {
        this.profiles = profiles;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}

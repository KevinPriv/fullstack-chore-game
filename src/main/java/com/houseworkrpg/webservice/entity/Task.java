package com.houseworkrpg.webservice.entity;

import javax.persistence.*;

/**
 * Tasks table
 */
@Entity
@Table(name = "task")
public class Task {

    // primary key for the task table
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String name;

    // amount of exp task gives
    @Column(nullable = false)
    private Integer exp;

    // description of task
    @Column(nullable = true, length = 256)
    private String description;

    // is the task completed and confirmed by a moderator
    @Column(nullable = false)
    private Boolean confirmed = false;

    // is the task completed
    @Column(nullable = false)
    private Boolean completed = false;

    // profile of the person who completed the task (can be null)
    @OneToOne
    @JoinColumn(name = "profile_id")
    private PlayerProfile profile;

    public PlayerProfile getProfile() {
        return profile;
    }

    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

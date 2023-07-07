package com.houseworkrpg.webservice.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Game profile data for each user
 */
@Entity
@Table(name = "player_profile")
public class PlayerProfile {
    // primary key for the user table
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // username (is unique)
    @Column(nullable = false, unique = true)
    private String name;

    // if they are a moderator of the group
    @Column(nullable = false)
    private Boolean moderator = false;

    // amount of exp points they have
    @Column(nullable = false)
    private Integer exp = 10;

    // strength stat
    @Column(nullable = false)
    private Integer strength = 1;

    // health stat
    @Column(nullable = false)
    private Integer hp = 1;

    // magic stat
    @Column(nullable = false)
    private Integer magic = 1;

    // speed stat
    @Column(nullable = false)
    private Integer agility = 1;

    // login data
    @OneToOne(mappedBy="profile")
    private User user;

    // tasks user has completed
    @OneToMany(mappedBy="profile")
    private Collection<Task> completedTasks;

    // group the user is currently in
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

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

    public Boolean getModerator() {
        return moderator;
    }

    public void setModerator(Boolean moderator) {
        this.moderator = moderator;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getMagic() {
        return magic;
    }

    public void setMagic(Integer magic) {
        this.magic = magic;
    }

    public Integer getAgility() {
        return agility;
    }

    public void setAgility(Integer agility) {
        this.agility = agility;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Task> getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Collection<Task> completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}

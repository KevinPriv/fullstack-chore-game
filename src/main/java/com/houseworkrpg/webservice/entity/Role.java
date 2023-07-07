package com.houseworkrpg.webservice.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Permissions table
 */
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // name of the role
    @Column(nullable = false, length = 16)
    private String name;

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

}
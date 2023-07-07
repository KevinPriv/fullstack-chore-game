package com.houseworkrpg.webservice.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * DTO for tasks page
 */
public class TaskDto {

    @NotNull
    @NotEmpty
    private String taskName;

    @NotNull
    @NotEmpty
    private String expAmount;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(String expAmount) {
        this.expAmount = expAmount;
    }
}

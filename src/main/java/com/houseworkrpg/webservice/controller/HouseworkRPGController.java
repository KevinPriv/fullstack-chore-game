package com.houseworkrpg.webservice.controller;

import com.houseworkrpg.webservice.dto.TaskDto;
import com.houseworkrpg.webservice.entity.*;
import com.houseworkrpg.webservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

/**
 * Controls game related pages
 */
@Controller
public class HouseworkRPGController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerProfileRepository playerRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private GroupRepository groupRepository;


    /**
     * Home page
     * @param model
     * @return home page
     */
    @GetMapping(value = {"/", "/home"})
    public String showHome(final Model model){
        return "home";
    }

    /**
     * Game page, where the sprites fight
     * @param model
     * @return
     */
    @GetMapping("game")
    public String showGame(final Model model){
        addGameAttributes(model);
        return "game";
    }

    /**
     * Increase a particular stat (POST REQUEST)
     * @param model
     * @param stat the stat you would like to increase
     * @return
     */
    @PostMapping("increase_stat")
    public String increaseStat(final Model model, @RequestParam(name="stat", required = true) String stat){
        if(stat == null) {
            return "redirect:/stats";
        }
        User user = getUser();
        PlayerProfile profile = user.getProfile();
        if(profile.getExp() > 0) {
            // increase stat
            switch(stat) {
                case "strength":
                    profile.setStrength(profile.getStrength() + 1);
                    break;
                case "hp":
                    profile.setHp(profile.getHp() + 1);
                    break;
                case "magic":
                    profile.setMagic(profile.getMagic() + 1);
                    break;
                case "agility":
                    profile.setAgility(profile.getAgility() + 1);
                    break;
                default:
                    // redirect to stats page if request sent is not a valid stat
                    return "redirect:/stats";
            }
            // decrease exp
            profile.setExp(profile.getExp() - 1);
            // save to database
            playerRepository.save(profile);
        }
        return "redirect:/stats";
    }

    /**
     * Shows stats page where you can increase stats
     * @param model
     * @return
     */
    @GetMapping("stats")
    public String showStats(final Model model){
        addGameAttributes(model);
        return "stats";
    }

    /**
     * Shows manager page
     * @param model
     * @return
     */
    @GetMapping("manager")
    public String showManager(final Model model){
        User user = getUser();
        PlayerProfile profile = user.getProfile();
        // validation to make sure users wont access the manager page (checks moderator permissions)
        if(!profile.getModerator()) {
            return "redirect:/error";
        }
        TaskDto taskDto = new TaskDto();
        model.addAttribute("task", taskDto);

        addGameAttributes(model);
        return "manager";
    }

    /**
     * Creates task for the household (POST REQUEST)
     * @param model
     * @param taskDto
     * @return
     */
    @PostMapping("add_task")
    public String addTask(final Model model, @ModelAttribute("task") TaskDto taskDto){
        if(taskDto.getTaskName().isEmpty()) {
            return "redirect:/manager";
        }
        User user = getUser();
        PlayerProfile profile = user.getProfile();
        Group group = profile.getGroup();
        // create task
        Task task = new Task();
        task.setName(taskDto.getTaskName());
        task.setDescription(taskDto.getTaskName());
        task.setCompleted(false);
        task.setConfirmed(false);
        task.setExp(Integer.valueOf(taskDto.getExpAmount()));
        group.getTasks().add(task);
        // save to database
        taskRepository.save(task);
        groupRepository.save(group);
        return "redirect:/manager";
    }

    /**
     * Shows task page
     * @param model
     * @return
     */
    @GetMapping("tasks")
    public String showTasks(final Model model){
        addGameAttributes(model);
        return "tasks";
    }


    /**
     * Sets task to finished (POST request)
     * @param model
     * @param id id of task in databaase
     * @return
     */
    @PostMapping("finished_task")
    public String finishedTask(final Model model,@RequestParam(name="id", required = true) String id){
        long finishedTaskId = Long.parseLong(id);
        Optional<Task> taskOp = taskRepository.findById(finishedTaskId);
        if(taskOp.isPresent()) {
            Task task = taskOp.get();
            task.setConfirmed(false);
            task.setCompleted(true);
            User user = getUser();
            PlayerProfile profile = user.getProfile();
            task.setProfile(profile);
            taskRepository.save(task);
            return "redirect:/tasks";
        }
        return "redirect:/error";
    }

    /**
     * Sets task to confirmed (POST request)
     * @param model
     * @param id id of task in database
     * @return
     */
    @PostMapping("confirm_task")
    public String confirmTask(final Model model,@RequestParam(name="id", required = true) String id){
        long confirmTaskId = Long.parseLong(id);
        Optional<Task> taskOp = taskRepository.findById(confirmTaskId);
        if(taskOp.isPresent()) {
            Task task = taskOp.get();
            task.setCompleted(true);
            task.setConfirmed(true);
            PlayerProfile profile = task.getProfile();
            profile.setExp(profile.getExp() + task.getExp());
            taskRepository.save(task);
            return "redirect:/manager";
        }
        return "redirect:/error";
    }

    /**
     * Sets task as not complete and removes user profile from the task (POST request)
     * @param model
     * @param id id of task
     * @return
     */
    @PostMapping("not_complete_task")
    public String notCompleteTask(final Model model,@RequestParam(name="id", required = true) String id){
        long notCompleteTaskId = Long.parseLong(id);
        Optional<Task> taskOp = taskRepository.findById(notCompleteTaskId);
        if(taskOp.isPresent()) {
            Task task = taskOp.get();
            task.setCompleted(false);
            task.setConfirmed(false);
            task.setProfile(null);
            taskRepository.save(task);
            return "redirect:/manager";
        }
        return "redirect:/error";
    }


    /**
     * Adds game variable attributes to thymeleaf page
     * @param model page you want the attributes added to
     */
    private void addGameAttributes(Model model) {
        User user = getUser();
        PlayerProfile profile = user.getProfile();
        Group group = profile.getGroup();
        Collection<PlayerProfile> players = group.getProfiles();
        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        model.addAttribute("group", group);
        int index = (int)(Math.random() * players.size());
        model.addAttribute("randomProfile", new ArrayList<>(players).get(index));
        model.addAttribute("players", players);
        model.addAttribute("tasks", group.getTasks());
        model.addAttribute("completedTasks", profile.getCompletedTasks());

    }

    /**
     * Gets current logged in user from database
     * @return current logged in user
     */
    public User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsername(username);
    }
}

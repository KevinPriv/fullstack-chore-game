package com.houseworkrpg.webservice.controller;

import com.houseworkrpg.webservice.dto.UserDto;
import com.houseworkrpg.webservice.entity.Group;
import com.houseworkrpg.webservice.entity.PlayerProfile;
import com.houseworkrpg.webservice.entity.User;
import com.houseworkrpg.webservice.repository.GroupRepository;
import com.houseworkrpg.webservice.repository.PlayerProfileRepository;
import com.houseworkrpg.webservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Random;


/**
 * Controls user login and register pages
 */
@Controller
public class UserController {
    // login user repository
    @Autowired
    private UserRepository userRepository;

    // game player repository
    @Autowired
    private PlayerProfileRepository playerRepository;

    // household group repository
    @Autowired
    private GroupRepository groupRepository;

    // our password encryptor
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Shows login page
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public String showLoginForm(final Model model){
        return "login";
    }


    /**
     * Shows registration form
     * @param model
     * @return
     */
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    /**
     * Registers user from the registration dto provided by the [#showRegistrationForm]
     * @param model
     * @param registrationDto
     * @return login page or error
     */
    @PostMapping("/register_user")
    public String registerUserAccount(Model model, @ModelAttribute("user") UserDto registrationDto) {
        // verification
        if(!registrationDto.getPassword().equals(registrationDto.getMatchingPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        if(registrationDto.getPassword().length() <= 6) {
            model.addAttribute("error", "Passwords is too short.");
            return "register";
        }

        if(registrationDto.getUsername().length() <= 3) {
            model.addAttribute("error", "Username is too short.");
            return "register";
        }

        if(registrationDto.getPassword().length() >= 32) {
            model.addAttribute("error", "Passwords is too long.");
            return "register";
        }

        if(registrationDto.getUsername().length() >= 32) {
            model.addAttribute("error", "Username is too long.");
            return "register";
        }

        if(userRepository.findByUsername(registrationDto.getUsername()) != null) {
            model.addAttribute("error", "You already have an account.");
            return "register";
        }


        // game profile
        PlayerProfile profile = new PlayerProfile();
        profile.setName(registrationDto.getUsername());
        if(!registrationDto.getCode().isEmpty()){
            Group joinGroup = groupRepository.findByCode(registrationDto.getCode());
            if(joinGroup == null) {
                model.addAttribute("error", "Invalid group code.");
                return "register";
            }

            // add profile to group
            profile.setGroup(joinGroup);
            // user login
            User user = new User();
            String encodedPassword = bCryptPasswordEncoder.encode(registrationDto.getPassword());
            user.setUsername(registrationDto.getUsername());
            user.setPassword(encodedPassword);
            user.setProfile(profile);
            playerRepository.save(profile);
            userRepository.save(user);
        } else { // must be creating a household
            Group group = new Group();
            if(registrationDto.getHouseName() != null || !registrationDto.getHouseName().isEmpty()) {
                group.setGroupName(registrationDto.getHouseName());
                group.setCode(generateUniqueHouseCode());
                // add player to group and set them as moderator
                profile.setModerator(true);
                profile.setGroup(group);
                // user login
                User user = new User();
                String encodedPassword = bCryptPasswordEncoder.encode(registrationDto.getPassword());
                user.setUsername(registrationDto.getUsername());
                user.setPassword(encodedPassword);
                user.setProfile(profile);
                groupRepository.save(group);
                playerRepository.save(profile);
                userRepository.save(user);
            } else {
                model.addAttribute("error", "No housecode or housename.");
                return "register";
            }
        }


        return "redirect:/login";
    }

    /**
     * Generates unique random 6 digit code from the alphabet
     * @return
     */
    private String generateUniqueHouseCode() {
        final String[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
        StringBuilder code;
        do {
            // generate 6 digit code
            code = new StringBuilder();
            for(int i=0;i<6;i++) {
                Random r = new Random();
                int randomIndex = r.nextInt(alphabet.length);
                code.append(alphabet[randomIndex]);
            }
            // make sure its unique or else generate it again
        } while(groupRepository.findByCode(code.toString()) != null);
        return code.toString();
    }


}
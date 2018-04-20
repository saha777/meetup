package com.meetup.meetup.rest.controller;


import com.meetup.meetup.service.ProfileService;
import com.meetup.meetup.service.vm.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{login}")
    public Profile getProfile(@PathVariable String login){
        return profileService.getProfile(login);
    }

    @PostMapping("/update")
    public String updateProfile(@RequestBody Profile updatedProfile){
        if(profileService.updateProfile(updatedProfile)){
            return "Success";
        }
        return "Don't updated";
    }

    @PostMapping("/friends")
    public List<Profile> getFriends(HttpServletRequest request){
        // TODO: 20.04.2018 make logic getting token
        Profile profile = profileService.getProfileFromToken(request.getHeader("Authorization"));
        return profileService.getFriends(profile.getId());
    }
}
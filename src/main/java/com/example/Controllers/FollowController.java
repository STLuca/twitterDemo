package com.example.Controllers;


import com.example.DataTransfer.UserDTO;
import com.example.Service.UserService;
import com.example.configuration.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user/{username}/")
public class FollowController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/follow")
    @ResponseStatus(HttpStatus.CREATED)
    public void followUser(@AuthenticationPrincipal CustomUser user, @PathVariable String username){
        userService.followUser(user.getUserID(), username);
    }

    @DeleteMapping(value = "/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollowUser(@AuthenticationPrincipal CustomUser user, @PathVariable String username){
        userService.unfollowUser(user.getUserID(), username);
    }

    @GetMapping(value = "following/old")
    public List<UserDTO> getOldFollowingUsers(@PathVariable String username,
                                              @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                              @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getFollowing(username, true, page, count );
    }

    @GetMapping(value = "following/new")
    public List<UserDTO> getNewFollowingUsers(@PathVariable String username,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getFollowing(username, false, page, count );
    }

    @GetMapping(value = "followers/old")
    public List<UserDTO> getOldFollowers(@PathVariable String username,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getFollowers(username, true, page, count );
    }

    @GetMapping(value = "followers/new")
    public List<UserDTO> getNewFollowers(@PathVariable String username,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getFollowers(username, false, page, count );
    }

}

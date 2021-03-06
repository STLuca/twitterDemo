package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.User;
import com.example.Service.UserService;
import com.example.configuration.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody User user){
         userService.saveUser(user);
         return user;
    }



    @GetMapping(value = "/{username}")
    public UserDTO getUser(@PathVariable String username, @AuthenticationPrincipal CustomUser customUser){
        return userService.getUser(username, customUser.getUserID());
    }

    @DeleteMapping(value = "/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username, @AuthenticationPrincipal CustomUser customUser){
        if (customUser.getUsername().equals(username)){
            userService.deleteUser(customUser.getUserID());
        }
    }

    @GetMapping(value = "{username}/tweets/recent/new")
    public CombinedDTO userNewTweets(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable String username,
                                     @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                     @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getRecentTweetsByUser(username, customUser.getUserID(), false, page, count);
    }

    @GetMapping(value = "{username}/tweets/recent/old")
    public CombinedDTO userOldTweets(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable String username,
                                           @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                           @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getRecentTweetsByUser(username, customUser.getUserID(), true, page, count);
    }

    @GetMapping(value = "{username}/tweets/liked/most")
    public CombinedDTO userMostLikedTweets(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable String username,
                                       @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                       @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                       @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getLikedTweetsByUser(username, customUser.getUserID(), false, dayLimit, page, count);
    }

    @GetMapping(value = "{username}/tweets/liked/least")
    public CombinedDTO userLeastLikedTweets(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable String username,
                                          @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                          @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getLikedTweetsByUser(username, customUser.getUserID(), true, dayLimit, page, count);
    }

    @GetMapping(value = "{username}/tweets/replied/most")
    public CombinedDTO userMostRepliedTweets(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable String username,
                                         @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                         @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                         @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getRepliedTweetsByUser(username, customUser.getUserID(), false, dayLimit, page, count);
    }

    @GetMapping(value = "{username}/tweets/replied/least")
    public CombinedDTO userLeastRepliedTweets(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable String username,
                                                @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                                @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getRepliedTweetsByUser(username, customUser.getUserID(), true, dayLimit, page, count);
    }
}

package com.example.Controllers;

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
    public UserDTO getUser(@PathVariable String username){
        return userService.getUser(username);
    }

    @DeleteMapping(value = "/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username, @AuthenticationPrincipal CustomUser customUser){
        userService.deleteUser(customUser.getUserID());
    }

    @GetMapping(value = "{username}/tweets/recent/")
    public List<TweetDTO> userRecentTweets(@PathVariable String username,
                                           @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                           @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getRecentTweetsByUser(username, page, count);
    }

    @GetMapping(value = "{username}/tweets/liked/")
    public List<TweetDTO> userLikedTweets(@PathVariable String username,
                                       @RequestParam(name = "t") int dayLimit,
                                       @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                       @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getMostLikedTweetsByUser(username, dayLimit, page, count);
    }

    @GetMapping(value = "{username}/tweets/replied/")
    public List<TweetDTO> userRepliedTweets(@PathVariable String username,
                                         @RequestParam(name = "t") int dayLimit,
                                         @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                         @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getMostRepliedTweetsByUser(username, dayLimit, page, count);
    }

}

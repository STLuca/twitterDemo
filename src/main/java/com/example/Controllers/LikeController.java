package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.Service.TweetService;
import com.example.Service.TweetServiceImp;
import com.example.Service.UserService;
import com.example.configuration.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LikeController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/tweet/{tweetID}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeTweet(@PathVariable Long tweetID, @AuthenticationPrincipal CustomUser user){
        userService.likeTweet(user.getUserID(), tweetID);
    }

    @DeleteMapping(value = "/tweet/{tweetID}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable Long tweetID, @AuthenticationPrincipal CustomUser user){
        userService.unlikeTweet(user.getUserID(), tweetID);
    }

    @GetMapping(value = "/user/{username}/tweets/likes/new")
    public CombinedDTO getUsersNewLikes(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable String username,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getUserLikes(username, customUser.getUserID(), false, page, count);
    }

    @GetMapping(value = "/user/{username}/tweets/likes/old")
    public CombinedDTO getUsersOldLikes(
            @AuthenticationPrincipal CustomUser customUser,
            @PathVariable String username,
                                           @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                           @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return userService.getUserLikes(username, customUser.getUserID(), true, page, count);
    }

}

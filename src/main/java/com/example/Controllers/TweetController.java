package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.Service.TweetService;
import com.example.Service.TweetServiceImp;
import com.example.configuration.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/tweet")
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public TweetDTO makeTweet(@AuthenticationPrincipal CustomUser customUser, @RequestBody String message){

        TweetDTO tweet = tweetService.saveTweet(customUser.getUserID(), message);
        return tweet;
    }

    @PostMapping(value = "/{tweetID}")
    @ResponseStatus(HttpStatus.CREATED)
    public TweetDTO replyToTweet(@PathVariable Long tweetID, @AuthenticationPrincipal CustomUser customUser, @RequestBody String message){
        TweetDTO tweet = tweetService.saveReplyTweet(customUser.getUserID(), message, tweetID);
        return tweet;
    }

    @GetMapping(value = "/{id}")
    public CombinedDTO getTweet(@AuthenticationPrincipal CustomUser customUser, @PathVariable Long id){
        CombinedDTO tweet = tweetService.getTweet(id, customUser.getUserID());
        return tweet;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTweet(@PathVariable Long id, @AuthenticationPrincipal CustomUser customUser){
        tweetService.deleteTweet(id, customUser.getUserID());
    }

    //LIKE REPLIES
    //Change tweet service to see old or new likes, atm only new likes

    @GetMapping(value = "/{id}/replies/likes/new")
    public CombinedDTO getNewTweetLikes(@PathVariable Long id,  @AuthenticationPrincipal CustomUser user){
        return tweetService.getLikedBy(id, user.getUserID());
    }

    @GetMapping(value = "/{id}/replies/likes/old")
    public CombinedDTO getOldTweetLikes(@PathVariable Long id, @AuthenticationPrincipal CustomUser user){
        return tweetService.getLikedBy(id, user.getUserID());
    }


    //TWEET REPLIES

    @GetMapping(value = "/{tweetID}/replies/recent/old")
    public CombinedDTO getRepliesOfTweetbyOldest(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getRecentTweetReplies(tweetID, user.getUserID(), true, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/recent/new")
    public CombinedDTO getRepliesOfTweetbyRecent(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getRecentTweetReplies(tweetID, user.getUserID(), false, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/liked/most")
    public CombinedDTO getRepliesOfTweetbyMostLikes(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getLikedTweetReplies(tweetID, user.getUserID(), false, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/liked/least")
    public CombinedDTO getRepliesOfTweetbyLeastLikes(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getLikedTweetReplies(tweetID, user.getUserID(), true, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/replied/most")
    public CombinedDTO getRepliesOfTweetbyMostReplies(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getRepliedTweetReplies(tweetID, user.getUserID(), false, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/replied/least")
    public CombinedDTO getRepliesOfTweetbyLeastReplies(
            @AuthenticationPrincipal CustomUser user,
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getRepliedTweetReplies(tweetID, user.getUserID(), true, page, count);
    }
}

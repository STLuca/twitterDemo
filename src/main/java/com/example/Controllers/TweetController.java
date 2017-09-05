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
    public TweetDTO getTweet(@PathVariable Long id){
        TweetDTO tweet = tweetService.getTweet(id);
        return tweet;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTweet(@PathVariable Long id, @AuthenticationPrincipal CustomUser customUser){
        tweetService.deleteTweet(id, customUser.getUserID());
    }

    @GetMapping(value = "/{id}/likes")
    public List<UserDTO> getTweetLikes(@PathVariable Long id){
        return tweetService.getLikedBy(id);
    }

    @GetMapping(value = "/{tweetID}/replies/old")
    public CombinedDTO getRepliesOfTweetbyOldest(
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getRecentTweetReplies(tweetID, true, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/new")
    public CombinedDTO getRepliesOfTweetbyRecent(
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getRecentTweetReplies(tweetID, false, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/liked/most")
    public CombinedDTO getRepliesOfTweetbyMostLikes(
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getLikedTweetReplies(tweetID, false, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/liked/least")
    public CombinedDTO getRepliesOfTweetbyLeastLikes(
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getLikedTweetReplies(tweetID, true, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/replied/most")
    public CombinedDTO getRepliesOfTweetbyMostReplies(
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getRepliedTweetReplies(tweetID, false, page, count);
    }

    @GetMapping(value = "/{tweetID}/replies/replied/least")
    public CombinedDTO getRepliesOfTweetbyLeastReplies(
            @PathVariable Long tweetID,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return tweetService.getRepliedTweetReplies(tweetID, true, page, count);
    }
}

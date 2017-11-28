package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.Entities.Tweet;
import com.example.Service.FeedService;
import com.example.Service.FeedServiceImp;
import com.example.configuration.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/feed")
public class FeedController {

    @Autowired
    FeedService feedService;

    @GetMapping(value = "/recent/new")
    public CombinedDTO getNewTweets(@AuthenticationPrincipal CustomUser user,
                                             @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                             @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return feedService.getRecentFeed(user.getUserID(), false, page, count);
    }

    @GetMapping(value = "/recent/old")
    public CombinedDTO getOldTweets(@AuthenticationPrincipal CustomUser user,
                                       @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                       @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return feedService.getRecentFeed(user.getUserID(), true, page, count);
    }

    @GetMapping(value = "/liked/most/")
    public CombinedDTO getMostLikedTweets(@AuthenticationPrincipal CustomUser user,
                                      @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "count", required = false, defaultValue = "20") Integer count){
        return feedService.getLikedFeed(user.getUserID(), false, dayLimit, page, count);
    }

    @GetMapping(value = "/liked/least/")
    public CombinedDTO getLeastLikedTweets(@AuthenticationPrincipal CustomUser user,
                                      @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "count", required = false, defaultValue = "20") Integer count){
        return feedService.getLikedFeed(user.getUserID(), true, dayLimit, page, count);
    }

    @GetMapping(value = "/replied/most")
    public CombinedDTO getMostRepliedTweets(@AuthenticationPrincipal CustomUser user,
                                        @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") Integer count){
        return feedService.getRepliedFeed(user.getUserID(), false, dayLimit, page, count);
    }

    @GetMapping(value = "/replied/least")
    public CombinedDTO getLeastRepliedTweets(@AuthenticationPrincipal CustomUser user,
                                        @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") Integer count){
        return feedService.getRepliedFeed(user.getUserID(), true, dayLimit, page, count);
    }

    @GetMapping(value = "/likes/new")
    public CombinedDTO getRecentLikedTweets(@AuthenticationPrincipal CustomUser user,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") Integer count){
        return feedService.getLikesFeed(user.getUserID(), false, page, count);
    }

    @GetMapping(value = "/likes/old")
    public CombinedDTO getOldLikedTweets(@AuthenticationPrincipal CustomUser user,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") Integer count){
        return feedService.getLikesFeed(user.getUserID(), true, page, count);
    }
}

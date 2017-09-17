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

    @GetMapping(value = "/recent/")
    public CombinedDTO getRecentTweets(@AuthenticationPrincipal CustomUser user,
                                             @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                             @RequestParam(name = "count", required = false, defaultValue = "20") int count){
        return feedService.getRecentFeed(user.getUserID(), page, count);
    }

    @GetMapping(value = "/liked/")
    public CombinedDTO getLikedTweets(@AuthenticationPrincipal CustomUser user,
                                      @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "count", required = false, defaultValue = "20") Integer count){
        return feedService.getMostLikedFeed(user.getUserID(), dayLimit, page, count);
    }

    @GetMapping(value = "/replied/")
    public CombinedDTO getRepliedTweets(@AuthenticationPrincipal CustomUser user,
                                        @RequestParam(name = "t", required = false, defaultValue = "1") int dayLimit,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") Integer count){
        return feedService.getMostRepliedFeed(user.getUserID(), dayLimit, page, count);
    }

    @GetMapping(value = "/likes/")
    public CombinedDTO getRepliedTweets(@AuthenticationPrincipal CustomUser user,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                        @RequestParam(name = "count", required = false, defaultValue = "20") Integer count){
        return feedService.getLikesFeed(user.getUserID(), page, count);
    }

}

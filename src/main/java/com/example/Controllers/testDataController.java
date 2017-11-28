package com.example.Controllers;


import com.example.DataTransfer.TweetDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.Service.FeedService;
import com.example.Service.TweetService;
import com.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/testData")
public class testDataController {

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private FeedService feedService;

    private Random rand = new Random();
    private List<String> names = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<TweetDTO> tweets = new ArrayList<>();
    private List<TweetDTO> replyTweets = new ArrayList<>();
    private final Long DAY_IN_MS = new Long(1000 * 60 * 60 * 24);

    @PostMapping
    public void populateWithTestData(){
        populateNames();
        populateTestUsers();
        populateTestTweets();
        populateReplyTweets();
        populateTestFollows();
        populateLikes();
    }

    private void populateNames(){
        names.add("Anna");
        names.add("Susan");
        names.add("Adam");
        names.add("Brian");
        names.add("Josh");

    }

    private void populateTestUsers(){

        for (String name: names){
            User user = new User(name, name + "@Gmail.com");
            user.setPassword("mypassword");
            userService.saveUser(user);
            users.add(user);
        }

    }

    private void populateTestTweets(){
        List<String> tweetKeywords = new ArrayList<>();
        tweetKeywords.add("first");
        tweetKeywords.add("second");

        for (String word: tweetKeywords){
            for (User user : users){
                tweets.add(tweetService.saveTweet(user.getId(), user.getUsername() + "'s " + word + " tweet"));
                waitXMilliseconds(101);
            }
        }

    }

    private void populateReplyTweets(){
        for (int i = 0; i <5; i++){
            for (User user : users){
                Long tweetID = tweets.get( rand.nextInt(tweets.size())).getId();
                replyTweets.add(tweetService.saveReplyTweet(user.getId(), user.getUsername() + " reply no. " + i + "!", tweetID));
                waitXMilliseconds(101);
            }
            tweets.addAll(replyTweets);
        }

    }

    private void populateTestFollows(){

        for (User user: users){

            for (User user2 : users){

                if (!user.equals(user2)){
                    if (rand.nextBoolean()){
                        userService.followUser(user.getId(), user2.getUsername());
                        waitXMilliseconds(101);
                    }
                }

            }

        }

    }

    private void populateLikes(){

        for (User user : users){

            for (TweetDTO tweet : tweets){
                if (rand.nextBoolean() && rand.nextBoolean()){
                    userService.likeTweet(user.getId(), tweet.getId());
                    waitXMilliseconds(101);
                }
            }

        }
    }

    private void waitXMilliseconds(int x){
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.example.DataTransfer;

import java.util.List;


public class CombinedDTO {

    private final List<UserDTO> users;
    private final List<TweetDTO> tweets;

    public CombinedDTO(List<UserDTO> users, List<TweetDTO> tweets) {
        this.users = users;
        this.tweets = tweets;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public List<TweetDTO> getTweets() {
        return tweets;
    }
}


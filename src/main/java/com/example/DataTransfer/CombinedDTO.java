package com.example.DataTransfer;

import java.util.List;

/**
 * Created by God Emperor on 28/08/2017.
 */
public class CombinedDTO {

    List<UserDTO> users;
    List<TweetDTO> tweets;

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


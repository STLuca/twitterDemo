package com.example.DataTransfer;

import java.util.ArrayList;
import java.util.List;


public class CombinedDTO {

    private final List<UserDTO> users;
    private final List<TweetDTO> tweets;

    public CombinedDTO(List<UserDTO> users, List<TweetDTO> tweets) {
        this.users = users;
        this.tweets = tweets;
    }

    public static CombinedDTO createFromUsers(List<UserDTO> users){
        return new CombinedDTO(users, new ArrayList<>());
    }

    public static CombinedDTO createFromTweets(List<TweetDTO> tweets){
        return new CombinedDTO(new ArrayList<>(), tweets);
    }

    public static CombinedDTO emptyCombinedDTO(){
        return new CombinedDTO(new ArrayList<>(), new ArrayList<>());
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public List<TweetDTO> getTweets() {
        return tweets;
    }

    @Override
    public boolean equals(Object o){
        if (o == this){return true;}
        if (!(o instanceof CombinedDTO)){return false;}
        CombinedDTO that = (CombinedDTO) o;
        return this.users.equals(that.users) &&
                this.tweets.equals(that.tweets);
    }

    @Override
    public int hashCode(){
        return users.hashCode() +
                tweets.hashCode();
    }
}


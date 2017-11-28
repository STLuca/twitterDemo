package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;

import java.util.List;


public interface UserService {

    UserDTO getUser(String username);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(Long userID);

    void followUser(Long userID, String followeeUsername);
    void unfollowUser(Long userID, String followeeUsername);
    CombinedDTO getFollowing(String username, boolean old, int page, int count);
    CombinedDTO getFollowers(String username, boolean old, int page, int count);

    void likeTweet(Long userID, Long tweetID);
    void unlikeTweet(Long userID, Long tweetID);

    //Returns list of tweets without user information, because only 1 user
    CombinedDTO getRecentTweetsByUser(String username, boolean asc, int page, int count);
    CombinedDTO getLikedTweetsByUser(String username, boolean asc, int withinDays, int page, int count);
    CombinedDTO getRepliedTweetsByUser(String username, boolean asc, int withinDays, int page, int count);

    CombinedDTO getUserLikes(String username, boolean old, int page, int count);
}

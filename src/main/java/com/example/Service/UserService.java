package com.example.Service;

import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;

import java.util.List;

/**
 * Created by God Emperor on 14/08/2017.
 */
public interface UserService {

    UserDTO getUser(String username);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(Long userID);

    void followUser(Long userID, String followeeUsername);
    void unfollowUser(Long userID, String followeeUsername);
    List<UserDTO> getFollowing(String username, boolean old, int page, int count);
    List<UserDTO> getFollowers(String username, boolean old, int page, int count);

    void likeTweet(Long userID, Long tweetID);
    void unlikeTweet(Long userID, Long tweetID);

    //Returns list of tweets without user information, because only 1 user
    List<TweetDTO> getRecentTweetsByUser(String username, int page, int count);
    List<TweetDTO> getMostLikedTweetsByUser(String username, int withinDays, int page, int count);
    List<TweetDTO> getMostRepliedTweetsByUser(String username, int withinDays, int page, int count);

    List<TweetDTO> getUserLikes(String username, boolean old, int page, int count);
}

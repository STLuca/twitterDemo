package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;

import java.util.List;

/**
 * Created by God Emperor on 15/08/2017.
 */
public interface TweetService {


    TweetDTO getTweet(Long id);
    TweetDTO saveTweet(Long userID, String message);
    TweetDTO saveReplyTweet(Long userID, String message, Long replyToId);
    void deleteTweet(Long tweetID, Long userID);

    List<UserDTO> getLikedBy(Long tweetID);

    //gets the tweets the user replied to and some replies
    //List<TweetDTO> getTweetOverview(Long tweetID);
    //List<TweetDTO> getParentTweets(Long tweetID);
    CombinedDTO getRecentTweetReplies(Long tweetID, boolean asc, int page, int count);
    CombinedDTO getLikedTweetReplies(Long tweetID, boolean asc, int page, int count);
    CombinedDTO getRepliedTweetReplies(Long tweetID, boolean asc, int page, int count);


}

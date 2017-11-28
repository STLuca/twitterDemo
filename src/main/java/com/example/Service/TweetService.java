package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;

import java.util.List;


public interface TweetService {


    TweetDTO getTweet(Long id);
    TweetDTO saveTweet(Long userID, String message);
    TweetDTO saveReplyTweet(Long userID, String message, Long replyToId);
    void deleteTweet(Long tweetID, Long userID);

    CombinedDTO getLikedBy(Long tweetID);

    CombinedDTO getRecentTweetReplies(Long tweetID, boolean asc, int page, int count);
    CombinedDTO getLikedTweetReplies(Long tweetID, boolean asc, int page, int count);
    CombinedDTO getRepliedTweetReplies(Long tweetID, boolean asc, int page, int count);


}

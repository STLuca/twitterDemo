package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;

import java.util.List;


public interface TweetService {


    CombinedDTO getTweet(Long id, Long myID);
    TweetDTO saveTweet(Long userID, String message);
    TweetDTO saveReplyTweet(Long userID, String message, Long replyToId);
    void deleteTweet(Long tweetID, Long userID);

    CombinedDTO getLikedBy(Long tweetID, Long myID);

    CombinedDTO getRecentTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count);
    CombinedDTO getLikedTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count);
    CombinedDTO getRepliedTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count);


}

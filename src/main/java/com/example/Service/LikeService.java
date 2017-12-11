package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;

import java.util.List;

public interface LikeService {

    void likeTweet(Long userID, Long tweetID);
    void unlikeTweet(Long userID, Long tweetID);
    CombinedDTO getUserLikes(String username, Long myID, boolean old, int page, int count);

}

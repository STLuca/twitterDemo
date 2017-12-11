package com.example.dao;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.Entities.Tweet;

import java.util.Date;
import java.util.List;


public interface JpaTweetRepository {

    Tweet findTweetByID(Long id);
    //Tweet find(Long id);
    void save(Tweet tweet);
    void update(Tweet tweet);
    void delete(Tweet tweet);

    List<TweetDTO> getTweet(Long tweetID, Long myID);

    void likeTweet(Long tweetID, Long myID, Date date);
    void unlikeTweet(Long tweetID, Long myID);

    List<TweetDTO> getRecentTweetsByUsers(List<Long> userIDs, Long myID, boolean asc, int page, int count);
    List<TweetDTO> getLikedTweetsByUsers(List<Long> userIDs, Long myID, boolean asc, Date withinDate, int page, int count);
    List<TweetDTO> getRepliedTweetsByUsers(List<Long> userIDs, Long myID, boolean asc, Date withinDate, int page, int count);
    List<TweetDTO> getUsersLikedTweets(List<Long> userIDs, Long myID, boolean old, int page, int count);

    List<TweetDTO> getRecentTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count);
    List<TweetDTO> getLikedTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count);
    List<TweetDTO> getRepliedTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count);
}

package com.example.dao;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.Entities.Tweet;

import java.util.Date;
import java.util.List;

/**
 * Created by God Emperor on 14/08/2017.
 */
public interface JpaTweetRepository {

    Tweet getReference(Long id);
    Tweet find(Long id);
    void save(Tweet tweet);
    void update(Tweet tweet);
    void delete(Tweet tweet);

    List<TweetDTO> getTweet(Long tweetID);

    List<TweetDTO> getRecentTweetsByUsers(List<Long> userIDs, int page, int count);
    List<TweetDTO> getMostLikedTweetsByUsers(List<Long> userIDs, Date withinDate, int page, int count);
    List<TweetDTO> getMostRepliedTweetsByUsers(List<Long> userIDs, Date withinDate, int page, int count);
    List<TweetDTO> getUsersLikedTweets(List<Long> userIDs, boolean old, int page, int count);

    List<TweetDTO> getRecentTweetReplies(Long tweetID, boolean asc, int page, int count);
    List<TweetDTO> getLikedTweetReplies(Long tweetID, boolean asc, int page, int count);
    List<TweetDTO> getRepliedTweetReplies(Long tweetID, boolean asc, int page, int count);
}

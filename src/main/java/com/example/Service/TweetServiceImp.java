package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Like;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import com.example.Exception.exceptions.InvalidOwnerException;
import com.example.Exception.exceptions.TweetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TweetServiceImp implements TweetService{

    @Autowired
    private JpaTweetRepository tweetRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Override
    public CombinedDTO getTweet(Long id, Long myID) {
        List<TweetDTO> tweet = tweetRepository.getTweet(id, myID);
        if (tweet.isEmpty()){
            throw new TweetNotFoundException(id);
        } else {
            Set<Long> userID = new HashSet<>();
            userID.add(tweet.get(0).getUserID());

            List<UserDTO> user = userRepository.getUsersByIDs(userID, myID);
            return new CombinedDTO(user, tweet);
        }
    }

    @Override
    public TweetDTO saveTweet(Long userID, String message) {
        User user = userRepository.findUserByID(userID);
        Tweet tweet = new Tweet(user, message);
        tweetRepository.save(tweet);
        return convertNewTweetToTweetDTO(tweet);
    }

    @Override
    public TweetDTO saveReplyTweet(Long userID, String message, Long replyToId) {
        User user = userRepository.findUserByID(userID);
        Tweet tweet = new Tweet(user, message);
        tweet.setRepliedTo(tweetRepository.findTweetByID(replyToId));
        tweetRepository.save(tweet);
        return convertNewTweetToTweetDTO(tweet);
    }

    private TweetDTO convertNewTweetToTweetDTO(Tweet tweet){
        Long replyTo = null;
        if (tweet.getRepliedTo() != null){
            replyTo = tweet.getRepliedTo().getId();
        }
        return new TweetDTO(tweet.getId(),
                            tweet.getUser().getId(),
                            tweet.getMessage(),
                            tweet.getCreationTimestamp(),
                            replyTo,
                            0, 0, false);
    }

    @Override
    public void deleteTweet(Long tweetID, Long userID) {
        Tweet tweet = tweetRepository.findTweetByID(tweetID);
        User userRequesting = userRepository.findUserByID(userID);
        if (tweet.getUser().equals(userRequesting)){
            tweetRepository.delete(tweet);
        } else {
            throw new InvalidOwnerException();
        }
    }

    @Override
    public CombinedDTO getLikedBy(Long tweetID, Long myID) {
        Tweet tweet = tweetRepository.findTweetByID(tweetID);
        Set<Long> ids = tweet.getLikedBy().stream().map(Like::getLikedBy).map(User::getId).collect(Collectors.toSet());
        if (ids.isEmpty()){
            return CombinedDTO.emptyCombinedDTO();      //<----------clean up sql query for empty result set
        }
        return CombinedDTO.createFromUsers(userRepository.getUsersByIDs(ids, myID));
    }

    @Override
    public CombinedDTO getRecentTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count) {
        List<TweetDTO> tweets = tweetRepository.getRecentTweetReplies(tweetID, myID, asc, page, count);
        if (tweets.isEmpty()){ return null; }
        List<UserDTO> users = userRepository.getUsersByIDs(getUserIDsFromTweets(tweets), myID);
        return new CombinedDTO(users, tweets);
    }

    @Override
    public CombinedDTO getLikedTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count) {
        List<TweetDTO> tweets = tweetRepository.getLikedTweetReplies(tweetID, myID, asc, page, count);
        if (tweets.isEmpty()){ return null; }
        List<UserDTO> users = userRepository.getUsersByIDs(getUserIDsFromTweets(tweets), myID);
        return new CombinedDTO(users, tweets);
    }

    @Override
    public CombinedDTO getRepliedTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count) {
        List<TweetDTO> tweets = tweetRepository.getRepliedTweetReplies(tweetID, myID, asc, page, count);
        if (tweets.isEmpty()){ return null; }
        List<UserDTO> users = userRepository.getUsersByIDs(getUserIDsFromTweets(tweets), myID);
        return new CombinedDTO(users, tweets);

    }

    private Set<Long> getUserIDsFromTweets(List<TweetDTO> tweets){
        return tweets.stream().map(TweetDTO::getUserID).collect(Collectors.toSet());
        //int x = 5;
        //return ids;
    }
}

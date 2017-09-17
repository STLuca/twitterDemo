package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Like;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import com.example.exception.exceptions.InvalidOwnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TweetServiceImp implements TweetService{

    @Autowired
    private JpaTweetRepository tweetRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Override
    public TweetDTO getTweet(Long id) {
        List<TweetDTO> tweet = tweetRepository.getTweet(id);
        if (tweet.isEmpty()){
            throw new EntityNotFoundException();
        } else {
            return tweet.get(0);
        }
    }

    @Override
    public TweetDTO saveTweet(Long userID, String message) {
        User user = userRepository.getReference(userID);
        Tweet tweet = new Tweet(user, message);
        tweetRepository.save(tweet);
        return convertNewTweetToTweetDTO(tweet);
    }

    @Override
    public TweetDTO saveReplyTweet(Long userID, String message, Long replyToId) {
        User user = userRepository.getReference(userID);
        Tweet tweet = new Tweet(user, message);
        tweet.setRepliedTo(tweetRepository.getReference(replyToId));
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
                            0, 0);
    }

    @Override
    public void deleteTweet(Long tweetID, Long userID) {
        Tweet tweet = tweetRepository.getReference(tweetID);
        User userRequesting = userRepository.getReference(userID);
        if (tweet.getUser().equals(userRequesting)){
            tweetRepository.delete(tweet);
        } else {
            throw new InvalidOwnerException();
        }
    }

    @Override
    public List<UserDTO> getLikedBy(Long tweetID) {
        Tweet tweet = tweetRepository.getReference(tweetID);
        List<User> users = new ArrayList<>();
        Set<Long> ids = tweet.getLikedBy().stream().map(Like::getLikedBy).map(User::getId).collect(Collectors.toSet());
        return userRepository.getUsersByIDs(ids);
    }

    @Override
    public CombinedDTO getRecentTweetReplies(Long tweetID, boolean asc, int page, int count) {
        List<TweetDTO> tweets = tweetRepository.getRecentTweetReplies(tweetID, asc, page, count);
        List<UserDTO> users = userRepository.getUsersByIDs(getUserIDsFromTweets(tweets));
        return new CombinedDTO(users, tweets);
    }

    @Override
    public CombinedDTO getLikedTweetReplies(Long tweetID, boolean asc, int page, int count) {
        List<TweetDTO> tweets = tweetRepository.getLikedTweetReplies(tweetID, asc, page, count);
        List<UserDTO> users = userRepository.getUsersByIDs(getUserIDsFromTweets(tweets));
        return new CombinedDTO(users, tweets);
    }

    @Override
    public CombinedDTO getRepliedTweetReplies(Long tweetID, boolean asc, int page, int count) {
        List<TweetDTO> tweets = tweetRepository.getRepliedTweetReplies(tweetID, asc, page, count);
        List<UserDTO> users = userRepository.getUsersByIDs(getUserIDsFromTweets(tweets));
        return new CombinedDTO(users, tweets);

    }

    private Set<Long> getUserIDsFromTweets(List<TweetDTO> tweets){
        return tweets.stream().map(TweetDTO::getUserID).collect(Collectors.toSet());
        //int x = 5;
        //return ids;
    }
}

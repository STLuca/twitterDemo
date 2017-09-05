package com.example.Service;


import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class UserServiceImp implements UserService {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaTweetRepository tweetRepository;

    private static final Long DAY_IN_MS = new Long(1000 * 60 * 60 * 24);

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    //might have to pass in user, check if tweet owner allows user to view tweet
    @Override
    public UserDTO getUser(String username){

        List<UserDTO> user = userRepository.getUserByUsername(username);
        if (user.isEmpty()){
            throw new EntityNotFoundException();
        } else {
            return user.get(0);
        }
    }

    @Override
    public void saveUser(User user ){

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

    }

    @Override
    public void updateUser(User user) {
        userRepository.update(user);
    }

    @Override
    public void deleteUser(Long userID){
        userRepository.removeById(userID);

    }

    @Override
    public void followUser(Long userID, String followeeUsername){

        User user = userRepository.getReference(userID);
        User followee = userRepository.findByUsername(followeeUsername);
        user.followUser(followee);

    }

    @Override
    public void unfollowUser(Long userID, String followeeUsername){
        User user = userRepository.getReference(userID);
        User followee = userRepository.findByUsername(followeeUsername);
        user.unfollowUser(followee);

    }

    @Override
    public List<UserDTO> getFollowing(String username, boolean old, int page, int count) {
        return userRepository.getFollowingByUsername(username, old, page, count);
    }

    @Override
    public List<UserDTO> getFollowers(String username, boolean old, int page, int count) {
        return userRepository.getFollowersByUsername(username, old, page, count);
    }

    @Override
    public void likeTweet(Long userID, Long tweetID){

        Tweet tweet = tweetRepository.getReference(tweetID);
        User user = userRepository.getReference(userID);
        user.likeTweet(tweet);

    }

    @Override
    public void unlikeTweet(Long userID, Long tweetID){
        Tweet tweet = tweetRepository.getReference(tweetID);
        User user = userRepository.getReference(userID);
        user.unlikeTweet(tweet);
    }


    @Override
    public List<TweetDTO> getRecentTweetsByUser(String username, int page, int count) {
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return tweetRepository.getRecentTweetsByUsers(userID, page, count);
    }

    @Override
    public List<TweetDTO> getMostLikedTweetsByUser(String username, int withinDays, int page, int count) {
        Date date = getDateXDaysAgo(withinDays);
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return tweetRepository.getMostLikedTweetsByUsers(userID, date, page, count);
    }

    @Override
    public List<TweetDTO> getMostRepliedTweetsByUser(String username, int withinDays, int page, int count) {
        Date date = getDateXDaysAgo(withinDays);
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return tweetRepository.getMostRepliedTweetsByUsers(userID, date, page, count);
    }

    @Override
    public List<TweetDTO> getUserLikes(String username, boolean old, int page, int count) {
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return tweetRepository.getUsersLikedTweets(userID, old, page, count);
    }


    private Date getDateXDaysAgo(int xDays){
        return new Date(System.currentTimeMillis() - (xDays * DAY_IN_MS));
    }

    private List<Long> getUserIDFromUsernameAsList(String username){
        User user = userRepository.findByUsername(username);
        return Arrays.asList(user.getId());
    }
}
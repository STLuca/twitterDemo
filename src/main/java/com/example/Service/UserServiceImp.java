package com.example.Service;


import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import com.example.Exception.exceptions.UserNotFoundException;
import com.example.Util.ITimeVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ITimeVariant timeVariant;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    //might have to pass in user, check if tweet owner allows user to view tweet
    @Override
    public UserDTO getUser(String username){

        List<UserDTO> user = userRepository.getUserByUsername(username);
        if (user.isEmpty()){
            throw new UserNotFoundException(username);
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

        User user = userRepository.findUserByID(userID);
        User followee = userRepository.findByUsername(followeeUsername);
        user.followUser(followee);

    }

    @Override
    public void unfollowUser(Long userID, String followeeUsername){
        User user = userRepository.findUserByID(userID);
        User followee = userRepository.findByUsername(followeeUsername);
        user.unfollowUser(followee);

    }

    @Override
    public CombinedDTO getFollowing(String username, boolean old, int page, int count) {
        return CombinedDTO.createFromUsers(userRepository.getFollowingByUsername(username, old, page, count));
    }

    @Override
    public CombinedDTO getFollowers(String username, boolean old, int page, int count) {
        return CombinedDTO.createFromUsers(userRepository.getFollowersByUsername(username, old, page, count));
    }

    @Override
    public void likeTweet(Long userID, Long tweetID){

        Tweet tweet = tweetRepository.findTweetByID(tweetID);
        User user = userRepository.findUserByID(userID);
        user.likeTweet(tweet);

    }

    @Override
    public void unlikeTweet(Long userID, Long tweetID){
        Tweet tweet = tweetRepository.findTweetByID(tweetID);
        User user = userRepository.findUserByID(userID);
        user.unlikeTweet(tweet);
    }


    @Override
    public CombinedDTO getRecentTweetsByUser(String username, boolean asc, int page, int count) {
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return CombinedDTO.createFromTweets(tweetRepository.getRecentTweetsByUsers(userID, asc, page, count));
    }

    @Override
    public CombinedDTO getLikedTweetsByUser(String username, boolean asc, int withinDays, int page, int count) {
        Date date = timeVariant.getDateFromXDaysAgo(withinDays);
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return CombinedDTO.createFromTweets(tweetRepository.getLikedTweetsByUsers(userID, asc, date, page, count));
    }

    @Override
    public CombinedDTO getRepliedTweetsByUser(String username, boolean asc, int withinDays, int page, int count) {
        Date date = timeVariant.getDateFromXDaysAgo(withinDays);
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return CombinedDTO.createFromTweets(tweetRepository.getRepliedTweetsByUsers(userID, asc, date, page, count));
    }

    @Override
    public CombinedDTO getUserLikes(String username, boolean old, int page, int count) {
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return CombinedDTO.createFromTweets(tweetRepository.getUsersLikedTweets(userID, old, page, count));
    }

    private List<Long> getUserIDFromUsernameAsList(String username){
        User user = userRepository.findByUsername(username);
        return Arrays.asList(user.getId());
    }
}
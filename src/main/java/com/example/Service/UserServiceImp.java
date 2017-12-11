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
    public UserDTO getUser(String username, Long myID){

        List<UserDTO> user = userRepository.getUserByUsername(username, myID);
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
    public void followUser(Long myID, String followeeUsername){

        //User user = userRepository.findUserByID(userID);
        //User followee = userRepository.findByUsername(followeeUsername);
        //user.followUser(followee);
        userRepository.followUser(userRepository.findByUsername(followeeUsername).getId(), myID);
    }

    @Override
    public void unfollowUser(Long myID, String followeeUsername){
        //User user = userRepository.findUserByID(userID);
        //User followee = userRepository.findByUsername(followeeUsername);
        //user.unfollowUser(followee);
        userRepository.unfollowUser(userRepository.findByUsername(followeeUsername).getId(), myID);
    }

    @Override
    public CombinedDTO getFollowing(String username, Long myID, boolean old, int page, int count) {
        return CombinedDTO.createFromUsers(userRepository.getFollowingByUsername(username, myID, old, page, count));
    }

    @Override
    public CombinedDTO getFollowers(String username, Long myID, boolean old, int page, int count) {
        return CombinedDTO.createFromUsers(userRepository.getFollowersByUsername(username, myID, old, page, count));
    }

    @Override
    public void likeTweet(Long userID, Long tweetID){

        //Tweet tweet = tweetRepository.findTweetByID(tweetID);
        //User user = userRepository.findUserByID(userID);
        //user.likeTweet(tweet);
        tweetRepository.likeTweet(tweetID, userID, new Date());
    }

    @Override
    public void unlikeTweet(Long userID, Long tweetID){
        //Tweet tweet = tweetRepository.findTweetByID(tweetID);
        //User user = userRepository.findUserByID(userID);
        //user.unlikeTweet(tweet);
        tweetRepository.unlikeTweet(tweetID, userID);
    }


    @Override
    public CombinedDTO getRecentTweetsByUser(String username, Long myID, boolean asc, int page, int count) {
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return CombinedDTO.createFromTweets(tweetRepository.getRecentTweetsByUsers(userID, myID, asc, page, count));
    }

    @Override
    public CombinedDTO getLikedTweetsByUser(String username, Long myID, boolean asc, int withinDays, int page, int count) {
        Date date = timeVariant.getDateFromXDaysAgo(withinDays);
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return CombinedDTO.createFromTweets(tweetRepository.getLikedTweetsByUsers(userID, myID, asc, date, page, count));
    }

    @Override
    public CombinedDTO getRepliedTweetsByUser(String username, Long myID, boolean asc, int withinDays, int page, int count) {
        Date date = timeVariant.getDateFromXDaysAgo(withinDays);
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return CombinedDTO.createFromTweets(tweetRepository.getRepliedTweetsByUsers(userID, myID, asc, date, page, count));
    }

    @Override
    public CombinedDTO getUserLikes(String username, Long myID, boolean old, int page, int count) {
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return CombinedDTO.createFromTweets(tweetRepository.getUsersLikedTweets(userID, myID, old, page, count));
    }

    private List<Long> getUserIDFromUsernameAsList(String username){
        User user = userRepository.findByUsername(username);
        return Arrays.asList(user.getId());
    }
}
package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class LikeServiceImp implements LikeService{


    private JpaUserRepository userRepository;

    private JpaTweetRepository tweetRepository;

    @Autowired
    public LikeServiceImp(JpaUserRepository userRepository, JpaTweetRepository tweetRepository){
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
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
    public CombinedDTO getUserLikes(String username, boolean old, int page, int count) {
        List<Long> userID = getUserIDFromUsernameAsList(username);
        return CombinedDTO.createFromTweets(tweetRepository.getUsersLikedTweets(userID, old, page, count));
    }

    private List<Long> getUserIDFromUsernameAsList(String username){
        User user = userRepository.findByUsername(username);
        return Arrays.asList(user.getId());
    }
}

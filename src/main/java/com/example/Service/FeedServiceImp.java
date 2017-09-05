package com.example.Service;

import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.DataTransfer.CombinedDTO;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by God Emperor on 18/08/2017.
 */
@Service
@Transactional
public class FeedServiceImp implements FeedService{

    private static final Long DAY_IN_MS = new Long(1000 * 60 * 60 * 24);

    @Autowired
    private JpaTweetRepository tweetRepository;

    @Autowired
    private JpaUserRepository userRepository;


    @Override
    public CombinedDTO getRecentFeed(Long id, int page, int count) {

        List<UserDTO> users = userRepository.getFollowingByID(id, false, 0, Integer.MAX_VALUE);
        List<TweetDTO> tweets = tweetRepository.getRecentTweetsByUsers(getIDsOfUsers(users), page, count);

        List<UserDTO> neededUsers = filterUnnecessaryUsers(users, tweets);
        return new CombinedDTO(neededUsers, tweets);
    }

    @Override
    public CombinedDTO getMostLikedFeed(Long id, int days, int page, int count) {

        Date date = getDateXDaysAgo(days);
        List<UserDTO> users = userRepository.getFollowingByID(id, false, 0, Integer.MAX_VALUE);
        List<TweetDTO> tweets = tweetRepository.getMostLikedTweetsByUsers(getIDsOfUsers(users), date, page, count);

        List<UserDTO> neededUsers = filterUnnecessaryUsers(users, tweets);
        return new CombinedDTO(neededUsers, tweets);
    }

    @Override
    public CombinedDTO getMostRepliedFeed(Long id,  int days, int page, int count) {

        Date date = getDateXDaysAgo(days);
        List<UserDTO> users = userRepository.getFollowingByID(id, false, 0, Integer.MAX_VALUE);

        List<TweetDTO> tweets = tweetRepository.getMostRepliedTweetsByUsers(getIDsOfUsers(users), date, page, count);
        List<UserDTO> neededUsers = filterUnnecessaryUsers(users, tweets);

        return new CombinedDTO(neededUsers, tweets);
    }

    @Override
    public CombinedDTO getLikesFeed(Long id, int page, int count) {

        List<UserDTO> users = userRepository.getFollowingByID(id, false, 0, Integer.MAX_VALUE);
        List<TweetDTO> tweets = tweetRepository.getUsersLikedTweets(getIDsOfUsers(users), false, page, count);
        List<UserDTO> neededUsers = filterUnnecessaryUsers(users, tweets);
        return new CombinedDTO(neededUsers, tweets);

    }

    private Date getDateXDaysAgo(int xDays){
        return new Date(System.currentTimeMillis() - (xDays * DAY_IN_MS));
    }

    private List<Long> getIDsOfUsers(List<UserDTO> users){
        return users.stream().map(UserDTO::getId).collect(Collectors.toList());
    }

    private List<UserDTO> filterUnnecessaryUsers(List<UserDTO> users, List<TweetDTO> tweetsBeingReturned){
        Set<Long> usersNeeded = getUserIDsFromTweets(tweetsBeingReturned);
        return users.stream()
                .filter((u) -> usersNeeded.contains(u.getId()))
                .collect(Collectors.toList());
    }

    private Set<Long> getUserIDsFromTweets(List<TweetDTO> tweets){
        return tweets.stream().map(TweetDTO::getUserID).collect(Collectors.toSet());
    }
}

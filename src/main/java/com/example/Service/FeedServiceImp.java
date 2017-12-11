package com.example.Service;

import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.DataTransfer.CombinedDTO;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import com.example.Util.ITimeVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class FeedServiceImp implements FeedService{

    @Autowired
    private JpaTweetRepository tweetRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private ITimeVariant timeVariant;

    @Override
    public CombinedDTO getRecentFeed(Long id, boolean asc, int page, int count) {

        List<UserDTO> users = getAllFollowedUsers(id);
        List<TweetDTO> tweets = tweetRepository.getRecentTweetsByUsers(getIDsOfUsers(users), id, asc, page, count);

        List<UserDTO> neededUsers = filterUnnecessaryUsers(users, tweets);
        return new CombinedDTO(neededUsers, tweets);
    }

    @Override
    public CombinedDTO getLikedFeed(Long id, boolean asc, int withinDays, int page, int count) {

        Date date = timeVariant.getDateFromXDaysAgo(withinDays);
        List<UserDTO> users = getAllFollowedUsers(id);
        List<TweetDTO> tweets = tweetRepository.getLikedTweetsByUsers(getIDsOfUsers(users), id, asc, date, page, count);

        List<UserDTO> neededUsers = filterUnnecessaryUsers(users, tweets);
        return new CombinedDTO(neededUsers, tweets);
    }

    @Override
    public CombinedDTO getRepliedFeed(Long id, boolean asc, int withinDays, int page, int count) {

        Date date = timeVariant.getDateFromXDaysAgo(withinDays);
        List<UserDTO> users = getAllFollowedUsers(id);
        List<TweetDTO> tweets = tweetRepository.getRepliedTweetsByUsers(getIDsOfUsers(users), id, asc, date, page, count);
        List<UserDTO> neededUsers = filterUnnecessaryUsers(users, tweets);

        return new CombinedDTO(neededUsers, tweets);
    }

    @Override
    public CombinedDTO getLikesFeed(Long id, boolean asc, int page, int count) {

        List<UserDTO> users = getAllFollowedUsers(id);
        List<TweetDTO> tweets = tweetRepository.getUsersLikedTweets(getIDsOfUsers(users), id, asc, page, count);
        List<UserDTO> neededUsers = filterUnnecessaryUsers(users, tweets);
        return new CombinedDTO(neededUsers, tweets);

    }

    private List<UserDTO> getAllFollowedUsers(Long userID){
        return userRepository.getFollowingByID(userID, userID, false, 0, Integer.MAX_VALUE);
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

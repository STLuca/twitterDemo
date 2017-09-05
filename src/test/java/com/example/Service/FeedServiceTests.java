package com.example.Service;

import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;


public class FeedServiceTests {

    private static final Long DAY_IN_MS = new Long(1000 * 60 * 60 * 24);

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaTweetRepository tweetRepository;

    @InjectMocks
    private FeedService userService = new FeedServiceImp();


    private List<UserDTO> userDTOList = new ArrayList<>();
    private List<TweetDTO> tweetDTOList = new ArrayList<>();

    public void init(){
        userDTOList.add(new UserDTO(new Long(1), "bob", 5, 2, 2));
        userDTOList.add(new UserDTO(new Long(2), "adam", 5, 2, 2));
        userDTOList.add(new UserDTO(new Long(3), "stewart", 5, 2, 2));

        Date date = new Date(System.currentTimeMillis() - (4 * DAY_IN_MS));
        tweetDTOList.add(new TweetDTO(new Long(5), new Long(1), "message by bob", date, null, 0, 0));
        tweetDTOList.add(new TweetDTO(new Long(6), new Long(2), "message by adam", date, null, 0, 0));
        tweetDTOList.add(new TweetDTO(new Long(7), new Long(3), "message by stewart in reply to bob", date, new Long(5), 0, 0));
        tweetDTOList.add(new TweetDTO(new Long(8), new Long(1), "message by bob in reply to stewart", date, new Long(7), 0, 0));
    }

    @Test
    public void testGetRecentFeed(){
        Long bobID = new Long(1);
        List<UserDTO> followedUsers = new ArrayList<>();
        followedUsers.add(userDTOList.get(1));
        followedUsers.add(userDTOList.get(2));
        when(userRepository.getFollowingByID(bobID, false, 0, Integer.MAX_VALUE))
                .thenReturn(followedUsers);

        List<TweetDTO> tweets = new ArrayList<>();
        tweets.add(tweetDTOList.get(1));
        tweets.add(tweetDTOList.get(2));
        List<Long> idsOfFollowedUsers = new ArrayList<>();
        idsOfFollowedUsers.add(new Long(2));
        idsOfFollowedUsers.add(new Long(3));

        when(tweetRepository.getRecentTweetsByUsers(idsOfFollowedUsers, 0, 20))
                .thenReturn(tweets);


    }
}

package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import com.example.Util.ITimeVariant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class FeedServiceTests {

    private static final Long DAY_IN_MS = new Long(1000 * 60 * 60 * 24);

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaTweetRepository tweetRepository;

    @Mock
    private ITimeVariant timeVariant;

    @InjectMocks
    private FeedService feedService = new FeedServiceImp();

    private List<Long> idsOfFollowedUsers;
    private List<UserDTO> userDTOList = new ArrayList<>();
    private List<TweetDTO> tweetDTOList = new ArrayList<>();
    private CombinedDTO expected;

    private static final Long myTestID = new Long(5);
    private static final Date date = new Date();

    @Before
    public void init(){
        idsOfFollowedUsers = Arrays.asList(new Long(1), new Long(2), new Long(3), new Long(4));

        userDTOList.add(new UserDTO(idsOfFollowedUsers.get(0), "bob", 5, 2, 2));
        userDTOList.add(new UserDTO(idsOfFollowedUsers.get(1), "adam", 5, 2, 2));
        userDTOList.add(new UserDTO(idsOfFollowedUsers.get(2), "stewart", 5, 2, 2));
        userDTOList.add(new UserDTO(idsOfFollowedUsers.get(3), "Mark", 1, 1, 1));

        tweetDTOList.add(new TweetDTO(new Long(5), new Long(1), "message by bob", date, null, 0, 0));
        tweetDTOList.add(new TweetDTO(new Long(6), new Long(2), "message by adam", date, null, 0, 0));
        tweetDTOList.add(new TweetDTO(new Long(7), new Long(3), "message by stewart in reply to bob", date, new Long(5), 0, 0));
        tweetDTOList.add(new TweetDTO(new Long(8), new Long(1), "message by bob in reply to stewart", date, new Long(7), 0, 0));

        expected = new CombinedDTO(userDTOList.subList(0, 3), tweetDTOList);

        when(userRepository.getFollowingByID(myTestID, false, 0, Integer.MAX_VALUE))
                .thenReturn(userDTOList);
    }

    @Test
    public void testGetRecentFeed(){

        when(tweetRepository.getRecentTweetsByUsers(idsOfFollowedUsers, false, 0, 20))
                .thenReturn(tweetDTOList);

        CombinedDTO actual = feedService.getRecentFeed(myTestID, false,0, 20);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLikedFeed(){

        when(timeVariant.getDateFromXDaysAgo(7))
                .thenReturn(date);

        when(tweetRepository.getLikedTweetsByUsers(idsOfFollowedUsers, false, date, 0, 20))
                .thenReturn(tweetDTOList);

        CombinedDTO actual = feedService.getLikedFeed(myTestID, false, 7, 0, 20);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRepliedFeed(){

        when(timeVariant.getDateFromXDaysAgo(7))
                .thenReturn(date);

        when(tweetRepository.getRepliedTweetsByUsers(idsOfFollowedUsers, false, date, 0, 20))
                .thenReturn(tweetDTOList);

        CombinedDTO actual = feedService.getRepliedFeed(myTestID, false, 7, 0, 20);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLikesFeed(){

        when(tweetRepository.getUsersLikedTweets(idsOfFollowedUsers, false, 0, 20))
                .thenReturn(tweetDTOList);

        CombinedDTO actual = feedService.getLikesFeed(myTestID, false, 0, 20);
        assertEquals(expected, actual);
    }

}

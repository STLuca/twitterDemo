package com.example.Service;

import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class LikeServiceTests {

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaTweetRepository tweetRepository;

    private LikeService likeService = new LikeServiceImp(userRepository, tweetRepository);

    private User testUser;
    private Tweet testTweet;
    private List<TweetDTO> testTweetDTOs = new ArrayList<>();

    @Before
    public void init(){

        testUser = spy(new User("bob", "bob@gmail.com"));
        when(testUser.getId()).thenReturn(new Long(1));

        testTweet = spy(new Tweet(testUser, "test tweet message"));
        when(testTweet.getId()).thenReturn(new Long(2));

        testTweetDTOs.add(new TweetDTO(new Long(5), new Long(1), "message by bob", new Date(), null, 0, 0, false));
    }

    @Test
    public void dummyTest(){

    }
}

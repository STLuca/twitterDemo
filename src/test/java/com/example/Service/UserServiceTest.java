package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.User;
import com.example.dao.JpaTweetRepository;
import com.example.dao.JpaUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {BCryptPasswordEncoder.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaTweetRepository tweetRepository;

    @Mock
    private User testUser;

    @InjectMocks
    private UserService userService = new UserServiceImp();

    private List<UserDTO> testUserDTOList = new ArrayList<>();
    private List<TweetDTO> tweetsDTOList = new ArrayList<>();
    //private User testUser;

    @Before
    public void init(){
        testUserDTOList.add( new UserDTO(new Long(1), "bob", 3, 5, 7));
        testUserDTOList.add(new UserDTO(new Long(2), "adam", 1, 2, 4));
        //testUser = new User("bob", "bob@gmail.com");
        //testUser.setPassword("myPassword");
        tweetsDTOList.add(new TweetDTO(new Long(4), new Long(1), "bob tweet", null, null, 1 ,5));
    }



    @Test
    public void testGetUserWithValidUser(){

        List<UserDTO> user = new ArrayList();
        user.add(testUserDTOList.get(0));
        when(userRepository.getUserByUsername("bob")).thenReturn(user);

        UserDTO returnedUser = userService.getUser("bob");
        assertEquals(returnedUser, testUserDTOList.get(0));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserWithInvalidUser(){

        List<UserDTO> user = new ArrayList();
        when(userRepository.getUserByUsername("susan")).thenReturn(user);

        UserDTO returnedUser = userService.getUser("susan");

    }

    @Test
    public void testGetRecentTweets(){
        List<UserDTO> user = new ArrayList();
        user.add(testUserDTOList.get(0));
        when(userRepository.findByUsername("bob")).thenReturn(testUser);
        when(testUser.getId()).thenReturn(new Long(1));

        List<Long> ids = new ArrayList<>();
        ids.add(new Long(1));
        when(tweetRepository.getRecentTweetsByUsers(ids, false, 0, 20)).thenReturn(tweetsDTOList);

        CombinedDTO tweets = userService.getRecentTweetsByUser("bob", false, 0, 20);
        assertEquals(tweets.getTweets(), tweetsDTOList);
    }

}

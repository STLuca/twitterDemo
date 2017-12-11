package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Service.UserService;
import com.example.TestConfig.CustomUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;



@RunWith(SpringRunner.class)
@Import(value = CustomUserDetailsService.class)
@WebMvcTest(value = UserController.class)
public class UserControllerIntegrationTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private UserDTO testUserDTO;
    private Date testDate;
    private List<TweetDTO> tweetDTOs = new ArrayList<>();
    private CombinedDTO tweetDTOContainer;

    //used from customUserDetailsService
    private final Long authUserID = new Long(1);

    @Before
    public void init(){

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                //.apply(springSecurity())
                .build();

        testDate = new Date();
        testUserDTO = new UserDTO(new Long(1), "Bob", 1, 2, 3, false);
        tweetDTOs.add(new TweetDTO(new Long(2), new Long(1), "my tweet message", testDate, null,1, 1, false));
        this.tweetDTOContainer = CombinedDTO.createFromTweets(tweetDTOs);
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUser() throws Exception{
        when(userService.getUser(testUserDTO.getUsername(), authUserID))
                .thenReturn(testUserDTO);


        mockMvc.perform(get("/user/" + testUserDTO.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(testUserDTO.getId()))
                .andExpect(jsonPath("$.username").value(testUserDTO.getUsername()))
                .andExpect(jsonPath("$.numOfTweets").value(testUserDTO.getNumOfTweets()))
                .andExpect(jsonPath("$.numOfFollowers").value(testUserDTO.getNumOfFollowers()))
                .andExpect(jsonPath("$.numOfFollowing").value(testUserDTO.getNumOfFollowing()));

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUserRecentTweets() throws Exception{
        when(userService.getRecentTweetsByUser(testUserDTO.getUsername(), authUserID, false, 0, 20))
                .thenReturn(tweetDTOContainer);
        TweetDTO tweet = tweetDTOs.get(0);


        mockMvc.perform(get("/user/" + testUserDTO.getUsername() + "/tweets/recent/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$['tweets'][0].id").value(tweet.getId()))
                .andExpect(jsonPath("$['tweets'][0].userID").value(tweet.getUserID()))
                .andExpect(jsonPath("$['tweets'][0].message").value(tweet.getMessage()))
                .andExpect(jsonPath("$['tweets'][0].timestamp").value(tweet.getTimestamp()))
                .andExpect(jsonPath("$['tweets'][0].replyTo").value(tweet.getReplyTo()))
                .andExpect(jsonPath("$['tweets'][0].numOfLikes").value(tweet.getNumOfLikes()))
                .andExpect(jsonPath("$['tweets'][0].numOfReplies").value(tweet.getNumOfReplies()));

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUserRecentTweetsWithParameters() throws Exception{
        when(userService.getRecentTweetsByUser(testUserDTO.getUsername(), authUserID, false, 1, 10))
                .thenReturn(tweetDTOContainer);
        TweetDTO tweet = tweetDTOs.get(0);


        mockMvc.perform(get("/user/" + testUserDTO.getUsername() + "/tweets/recent/new?page=1&count=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$['tweets'][0].id").value(tweet.getId()))
                .andExpect(jsonPath("$['tweets'][0].userID").value(tweet.getUserID()))
                .andExpect(jsonPath("$['tweets'][0].message").value(tweet.getMessage()))
                .andExpect(jsonPath("$['tweets'][0].timestamp").value(tweet.getTimestamp()))
                .andExpect(jsonPath("$['tweets'][0].replyTo").value(tweet.getReplyTo()))
                .andExpect(jsonPath("$['tweets'][0].numOfLikes").value(tweet.getNumOfLikes()))
                .andExpect(jsonPath("$['tweets'][0].numOfReplies").value(tweet.getNumOfReplies()));

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUserLikedTweetsNoParameters() throws Exception{
        when(userService.getLikedTweetsByUser(testUserDTO.getUsername(), authUserID, false, 1, 0, 20))
                .thenReturn(tweetDTOContainer);
        TweetDTO tweet = tweetDTOs.get(0);


        mockMvc.perform(get("/user/" + testUserDTO.getUsername() + "/tweets/liked/most"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$['tweets'][0].id").value(tweet.getId()))
                .andExpect(jsonPath("$['tweets'][0].userID").value(tweet.getUserID()))
                .andExpect(jsonPath("$['tweets'][0].message").value(tweet.getMessage()))
                .andExpect(jsonPath("$['tweets'][0].timestamp").value(tweet.getTimestamp()))
                .andExpect(jsonPath("$['tweets'][0].replyTo").value(tweet.getReplyTo()))
                .andExpect(jsonPath("$['tweets'][0].numOfLikes").value(tweet.getNumOfLikes()))
                .andExpect(jsonPath("$['tweets'][0].numOfReplies").value(tweet.getNumOfReplies()));

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUserLikedTweetsWithParameters() throws Exception{
        when(userService.getLikedTweetsByUser(testUserDTO.getUsername(), authUserID, false, 3,1, 10))
                .thenReturn(tweetDTOContainer);
        TweetDTO tweet = tweetDTOs.get(0);


        mockMvc.perform(get("/user/" + testUserDTO.getUsername() + "/tweets/liked/most?t=3&page=1&count=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$['tweets'][0].id").value(tweet.getId()))
                .andExpect(jsonPath("$['tweets'][0].userID").value(tweet.getUserID()))
                .andExpect(jsonPath("$['tweets'][0].message").value(tweet.getMessage()))
                .andExpect(jsonPath("$['tweets'][0].timestamp").value(tweet.getTimestamp()))
                .andExpect(jsonPath("$['tweets'][0].replyTo").value(tweet.getReplyTo()))
                .andExpect(jsonPath("$['tweets'][0].numOfLikes").value(tweet.getNumOfLikes()))
                .andExpect(jsonPath("$['tweets'][0].numOfReplies").value(tweet.getNumOfReplies()));

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUserRepliedTweets() throws Exception{
        when(userService.getRepliedTweetsByUser(testUserDTO.getUsername(), authUserID, false, 1,0, 20))
                .thenReturn(tweetDTOContainer);
        TweetDTO tweet = tweetDTOs.get(0);


        mockMvc.perform(get("/user/" + testUserDTO.getUsername() + "/tweets/replied/most"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$['tweets'][0].id").value(tweet.getId()))
                .andExpect(jsonPath("$['tweets'][0].userID").value(tweet.getUserID()))
                .andExpect(jsonPath("$['tweets'][0].message").value(tweet.getMessage()))
                .andExpect(jsonPath("$['tweets'][0].timestamp").value(tweet.getTimestamp()))
                .andExpect(jsonPath("$['tweets'][0].replyTo").value(tweet.getReplyTo()))
                .andExpect(jsonPath("$['tweets'][0].numOfLikes").value(tweet.getNumOfLikes()))
                .andExpect(jsonPath("$['tweets'][0].numOfReplies").value(tweet.getNumOfReplies()));

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUserRepliedTweetsWithParameters() throws Exception{
        when(userService.getRepliedTweetsByUser(testUserDTO.getUsername(), authUserID, false, 3,1, 10))
                .thenReturn(tweetDTOContainer);
        TweetDTO tweet = tweetDTOs.get(0);


        mockMvc.perform(get("/user/" + testUserDTO.getUsername() + "/tweets/replied/most?t=3&page=1&count=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$['tweets'][0].id").value(tweet.getId()))
                .andExpect(jsonPath("$['tweets'][0].userID").value(tweet.getUserID()))
                .andExpect(jsonPath("$['tweets'][0].message").value(tweet.getMessage()))
                .andExpect(jsonPath("$['tweets'][0].timestamp").value(tweet.getTimestamp()))
                .andExpect(jsonPath("$['tweets'][0].replyTo").value(tweet.getReplyTo()))
                .andExpect(jsonPath("$['tweets'][0].numOfLikes").value(tweet.getNumOfLikes()))
                .andExpect(jsonPath("$['tweets'][0].numOfReplies").value(tweet.getNumOfReplies()));

    }

}

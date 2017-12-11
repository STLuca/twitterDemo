package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Service.TweetService;
import com.example.TestConfig.CustomUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(value = CustomUserDetailsService.class)
@WebMvcTest(value = TweetController.class)
public class TweetControllerIntegrationTest {

    @MockBean
    private TweetService tweetService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private TweetDTO tweet;
    private List<UserDTO> users;
    private CombinedDTO usersDTOContainer;
    private CombinedDTO replies;

    //used from customUserDetailsService
    private final Long authUserID = new Long(1);

    @Before
    public void init(){

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                //.apply(springSecurity())
                .build();

        tweet = new TweetDTO(new Long(2), new Long(1), "my tweet message", new Date(), null,1, 1, false);
        users = Arrays.asList(new UserDTO(new Long(1), "Bob", 1, 2, 3, false));
        replies = new CombinedDTO(users, Arrays.asList(tweet));
        usersDTOContainer = CombinedDTO.createFromUsers(users);
    }

   /* @Test
    @WithUserDetails(value = "bob")
    public void testGetTweet() throws Exception{

        when(tweetService.getTweet(tweet.getId(), authUserID))
                .thenReturn(tweet);

        mockMvc.perform(get("/tweet/" + tweet.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }*/

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLikedByDefaultParameters() throws Exception {

        when(tweetService.getLikedBy(tweet.getId(), authUserID))
                .thenReturn(usersDTOContainer);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/likes/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLikedByCustomParameters() throws Exception {

        when(tweetService.getLikedBy(tweet.getId(), authUserID))
                .thenReturn(usersDTOContainer);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/likes/new")
                        .param("page", "1")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetOldestRepliesDefaultParameters() throws Exception{

        when(tweetService.getRecentTweetReplies(tweet.getId(), authUserID, true, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/recent/old"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetOldestRepliesCustomParameters() throws Exception{

        when(tweetService.getRecentTweetReplies(tweet.getId(), authUserID, true, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/recent/old")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetNewestRepliesDefaultParameters() throws Exception{

        when(tweetService.getRecentTweetReplies(tweet.getId(), authUserID, false, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/recent/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetNewestRepliesCustomParameters() throws Exception{

        when(tweetService.getRecentTweetReplies(tweet.getId(), authUserID, false, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/recent/new")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetMostLikedRepliesDefaultParameters() throws Exception{

        when(tweetService.getLikedTweetReplies(tweet.getId(), authUserID, false, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/liked/most"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetMostLikedRepliesCustomParameters() throws Exception{

        when(tweetService.getLikedTweetReplies(tweet.getId(), authUserID, false, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/liked/most")
                        .param("page", "1")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLeastLikedRepliesDefaultParameters() throws Exception{

        when(tweetService.getLikedTweetReplies(tweet.getId(), authUserID, true, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/liked/least"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLeastLikedRepliesCustomParameters() throws Exception{

        when(tweetService.getLikedTweetReplies(tweet.getId(), authUserID, true, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/liked/least")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /////////
    @Test
    @WithUserDetails(value = "bob")
    public void testGetMostRepliedRepliesDefaultParameters() throws Exception{

        when(tweetService.getRepliedTweetReplies(tweet.getId(), authUserID, false, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/replied/most"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetMostRepliedRepliesCustomParameters() throws Exception{

        when(tweetService.getRepliedTweetReplies(tweet.getId(), authUserID, false, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/replied/most")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLeastRepliedRepliesDefaultParameters() throws Exception{

        when(tweetService.getRepliedTweetReplies(tweet.getId(), authUserID, true, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/replied/least"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLeastRepliedRepliesCustomParameters() throws Exception{

        when(tweetService.getRepliedTweetReplies(tweet.getId(), authUserID, true, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/replied/least")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }


}

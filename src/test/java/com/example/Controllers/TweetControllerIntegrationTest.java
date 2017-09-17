package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Service.TweetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
@WebMvcTest(value = TweetController.class, secure = false)
public class TweetControllerIntegrationTest {

    @MockBean
    private TweetService tweetService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private TweetDTO tweet;
    private List<UserDTO> users;
    private CombinedDTO replies;

    @Before
    public void init(){

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                //.apply(springSecurity())
                .build();

        tweet = new TweetDTO(new Long(2), new Long(1), "my tweet message", new Date(), null,1, 1);
        users = Arrays.asList(new UserDTO(new Long(1), "Bob", 1, 2, 3));
        replies = new CombinedDTO(users, Arrays.asList(tweet));

    }

    @Test
    public void testGetTweet() throws Exception{

        when(tweetService.getTweet(tweet.getId()))
                .thenReturn(tweet);

        mockMvc.perform(get("/tweet/" + tweet.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    public void testGetLikedByDefaultParameters() throws Exception {

        when(tweetService.getLikedBy(tweet.getId()))
                .thenReturn(users);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/likes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    public void testGetLikedByCustomParameters() throws Exception {

        when(tweetService.getLikedBy(tweet.getId()))
                .thenReturn(users);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/likes")
                        .param("page", "1")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    public void testGetOldestRepliesDefaultParameters() throws Exception{

        when(tweetService.getRecentTweetReplies(tweet.getId(), true, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/old"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetOldestRepliesCustomParameters() throws Exception{

        when(tweetService.getRecentTweetReplies(tweet.getId(), true, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/old")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetNewestRepliesDefaultParameters() throws Exception{

        when(tweetService.getRecentTweetReplies(tweet.getId(), false, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetNewestRepliesCustomParameters() throws Exception{

        when(tweetService.getRecentTweetReplies(tweet.getId(), false, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/new")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetMostLikedRepliesDefaultParameters() throws Exception{

        when(tweetService.getLikedTweetReplies(tweet.getId(), false, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/liked/most"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetMostLikedRepliesCustomParameters() throws Exception{

        when(tweetService.getLikedTweetReplies(tweet.getId(), false, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/liked/most")
                        .param("page", "1")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetLeastLikedRepliesDefaultParameters() throws Exception{

        when(tweetService.getLikedTweetReplies(tweet.getId(), true, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/liked/least"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetLeastLikedRepliesCustomParameters() throws Exception{

        when(tweetService.getLikedTweetReplies(tweet.getId(), true, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/liked/least")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /////////
    @Test
    public void testGetMostRepliedRepliesDefaultParameters() throws Exception{

        when(tweetService.getRepliedTweetReplies(tweet.getId(), false, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/replied/most"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetMostRepliedRepliesCustomParameters() throws Exception{

        when(tweetService.getRepliedTweetReplies(tweet.getId(), false, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/replied/most")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetLeastRepliedRepliesDefaultParameters() throws Exception{

        when(tweetService.getRepliedTweetReplies(tweet.getId(), true, 0,20))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/replied/least"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetLeastRepliedRepliesCustomParameters() throws Exception{

        when(tweetService.getRepliedTweetReplies(tweet.getId(), true, 1,10))
                .thenReturn(replies);

        mockMvc.perform(get("/tweet/" + tweet.getId() + "/replies/replied/least")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}

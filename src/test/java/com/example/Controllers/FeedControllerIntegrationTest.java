package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Service.FeedService;
import com.example.TestConfig.CustomUserDetailsService;
import com.example.configuration.CustomUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@Import(value = CustomUserDetailsService.class)
@WebMvcTest(value = FeedController.class)
public class FeedControllerIntegrationTest {

    @MockBean
    private FeedService feedService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserDetailsService customUserService;

    private MockMvc mockMvc;

    private CombinedDTO dto;

    @Before
    public void init(){

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();

        List<UserDTO> users = new ArrayList<>();
        List<TweetDTO> tweets = new ArrayList<>();
        users.add(new UserDTO(new Long(1), "Bob", 1, 2, 3));
        tweets.add(new TweetDTO(new Long(2), new Long(1), "my tweet message", new Date(), null,1, 1));
        dto = new CombinedDTO(users, tweets);
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetRecentFeedDefaultParameters() throws Exception{

        when(feedService.getRecentFeed( (new Long(1)), false, 0, 20))
                .thenReturn(dto);

        mockMvc.perform(get("/feed/recent/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.tweets").isArray());

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetRecentFeedCustomParameters() throws Exception{

        when(feedService.getRecentFeed( (new Long(1)), false, 1, 10))
                .thenReturn(dto);

        mockMvc.perform(get("/feed/recent/")
                            .param("page", "1")
                            .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.tweets").isArray());

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLikedFeedDefaultParameters() throws Exception{

        when(feedService.getLikedFeed( (new Long(1)), false, 1, 0, 20))
                .thenReturn(dto);

        mockMvc.perform(get("/feed/liked/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.tweets").isArray());

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLikedFeedCustomParameters() throws Exception{

        when(feedService.getLikedFeed( (new Long(1)), false, 3, 1, 10))
                .thenReturn(dto);

        mockMvc.perform(get("/feed/liked/")
                .param("t", "3")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.tweets").isArray());

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetRepliedFeedDefaultParameters() throws Exception{

        when(feedService.getRepliedFeed( (new Long(1)), false, 1, 0, 20))
                .thenReturn(dto);

        mockMvc.perform(get("/feed/replied/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.tweets").isArray());

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetRepliedFeedCustomParameters() throws Exception{

        when(feedService.getRepliedFeed( (new Long(1)), false, 3, 1, 10))
                .thenReturn(dto);

        mockMvc.perform(get("/feed/replied/")
                .param("page", "1")
                .param("count", "10")
                .param("t", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.tweets").isArray());

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLikesFeedDefaultParameters() throws Exception{

        when(feedService.getLikesFeed( (new Long(1)), false, 0, 20))
                .thenReturn(dto);

        mockMvc.perform(get("/feed/likes/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.tweets").isArray());

    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetLikesFeedCustomParameters() throws Exception{

        when(feedService.getLikesFeed( (new Long(1)), false, 1, 10))
                .thenReturn(dto);

        mockMvc.perform(get("/feed/likes/")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.tweets").isArray());

    }
}

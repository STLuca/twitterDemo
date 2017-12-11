package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.Service.UserService;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(value = CustomUserDetailsService.class)
@WebMvcTest(value = LikeController.class)
public class LikeControllerIntegrationTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private String username;
    private List<TweetDTO> tweets;
    private CombinedDTO dtoContainer;

    //used from customUserDetailsService
    private final Long authUserID = new Long(1);

    @Before
    public void init() {

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                //.apply(springSecurity())
                .build();

        username = "bob";
        tweets = Arrays.asList(new TweetDTO(new Long(2), new Long(1), "my tweet message", new Date(), null,1, 1, false));
        dtoContainer = CombinedDTO.createFromTweets(tweets);
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUsersNewLikesDefaultParameters() throws Exception{
        when(userService.getUserLikes(username, authUserID,false, 0, 20))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + username + "/tweets/likes/new/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUsersNewLikesCustomParameters() throws Exception{
        when(userService.getUserLikes(username, authUserID, false, 1, 10))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + username + "/tweets/likes/new/")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUsersOldLikesDefaultParameters() throws Exception{
        when(userService.getUserLikes(username, authUserID, true, 0, 20))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + username + "/tweets/likes/old/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testGetUsersOldLikesCustomParameters() throws Exception{
        when(userService.getUserLikes(username, authUserID, true, 1, 10))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + username + "/tweets/likes/old/")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isNotEmpty());
    }

}

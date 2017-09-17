package com.example.Controllers;

import com.example.DataTransfer.UserDTO;
import com.example.Service.UserService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = FollowController.class, secure = false)
public class FollowControllerIntegrationTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private UserDTO user;
    private List<UserDTO> users;

    @Before
    public void init(){

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                //.apply(springSecurity())
                .build();

        user = new UserDTO(new Long(1), "Bob", 1, 2, 3);
        users = Arrays.asList(user);
    }

    @Test
    public void testOldFollowersDefaultParameters() throws Exception{
        when(userService.getFollowers(user.getUsername(), true, 0, 20))
                .thenReturn(users);

        mockMvc.perform(get("/user/" + user.getUsername() + "/followers/old"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testOldFollowersCustomParameters() throws Exception{
        when(userService.getFollowers(user.getUsername(), true, 1, 10))
                .thenReturn(users);

        mockMvc.perform(get("/user/" + user.getUsername() + "/followers/old")
                            .param("page", "1")
                            .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testNewFollowersDefaultParameters() throws Exception{
        when(userService.getFollowers(user.getUsername(), false, 0, 20))
                .thenReturn(users);

        mockMvc.perform(get("/user/" + user.getUsername() + "/followers/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testNewFollowersCustomParameters() throws Exception{
        when(userService.getFollowers(user.getUsername(), false, 1, 10))
                .thenReturn(users);

        mockMvc.perform(get("/user/" + user.getUsername() + "/followers/new")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testOldFolloweesDefaultParameters() throws Exception{
        when(userService.getFollowing(user.getUsername(), false, 0, 20))
                .thenReturn(users);

        mockMvc.perform(get("/user/" + user.getUsername() + "/following/old"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testOldFolloweesCustomParameters() throws Exception{
        when(userService.getFollowing(user.getUsername(), true, 1, 10))
                .thenReturn(users);

        mockMvc.perform(get("/user/" + user.getUsername() + "/following/old")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testNewFolloweesDefaultParameters() throws Exception{
        when(userService.getFollowing(user.getUsername(), true, 0, 20))
                .thenReturn(users);

        mockMvc.perform(get("/user/" + user.getUsername() + "/following/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testNewFolloweesCustomParameters() throws Exception{
        when(userService.getFollowing(user.getUsername(), true, 1, 10))
                .thenReturn(users);

        mockMvc.perform(get("/user/" + user.getUsername() + "/following/new")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }


}

package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.UserDTO;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(value = CustomUserDetailsService.class)
@WebMvcTest(value = FollowController.class)
public class FollowControllerIntegrationTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private UserDTO user;
    private List<UserDTO> users;
    private CombinedDTO dtoContainer;

    //used from customUserDetailsService
    private final Long authUserID = new Long(1);


    @Before
    public void init(){

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                //.apply(springSecurity())
                .build();

        user = new UserDTO(new Long(1), "Bob", 1, 2, 3, false);
        users = Arrays.asList(user);
        dtoContainer = CombinedDTO.createFromUsers(users);
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testOldFollowersDefaultParameters() throws Exception{
        when(userService.getFollowers(user.getUsername(), authUserID, true, 0, 20))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + user.getUsername() + "/follows/followers/old"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testOldFollowersCustomParameters() throws Exception{
        when(userService.getFollowers(user.getUsername(), authUserID, true, 1, 10))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + user.getUsername() + "/follows/followers/old")
                            .param("page", "1")
                            .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testNewFollowersDefaultParameters() throws Exception{
        when(userService.getFollowers(user.getUsername(), authUserID, false, 0, 20))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + user.getUsername() + "/follows/followers/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testNewFollowersCustomParameters() throws Exception{
        when(userService.getFollowers(user.getUsername(), authUserID, false, 1, 10))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + user.getUsername() + "/follows/followers/new")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testOldFolloweesDefaultParameters() throws Exception{
        when(userService.getFollowing(user.getUsername(), authUserID, true, 0, 20))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + user.getUsername() + "/follows/following/old"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testOldFolloweesCustomParameters() throws Exception{
        when(userService.getFollowing(user.getUsername(), authUserID, true, 1, 10))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + user.getUsername() + "/follows/following/old")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testNewFolloweesDefaultParameters() throws Exception{
        when(userService.getFollowing(user.getUsername(), authUserID, false, 0, 20))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + user.getUsername() + "/follows/following/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(value = "bob")
    public void testNewFolloweesCustomParameters() throws Exception{
        when(userService.getFollowing(user.getUsername(), authUserID, false, 1, 10))
                .thenReturn(dtoContainer);

        mockMvc.perform(get("/user/" + user.getUsername() + "/follows/following/new")
                .param("page", "1")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }


}

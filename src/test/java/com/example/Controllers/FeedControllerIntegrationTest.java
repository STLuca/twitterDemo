package com.example.Controllers;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Service.FeedService;
import com.example.configuration.CustomUser;
import com.example.configuration.myWebConfiguration;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ContextConfiguration(classes = {myWebConfiguration.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class FeedControllerIntegrationTest {

    @Mock
    FeedService feedService;


    private MockMvc mockMvc;

    private CustomUser userDetails;

    private List<UserDTO> userDTO = new ArrayList<>();
    private List<TweetDTO> tweetDTO = new ArrayList<>();

    private CustomUser createCustomUser(Long id, String username){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUser(id, username, "encryptedPassword", authorities);
    }

    @Before
    public void init(){



        Long userID = new Long(1);
        String username = "bob";

        userDetails = createCustomUser(userID, "bob");
        userDTO.add(new UserDTO(userID, "bob", 5, 2, 2));
        tweetDTO.add(new TweetDTO(new Long(5), userID, "message by bob", null, null, 0, 0));
    }

    public void testGetRecentTweets(){
        when(feedService.getRecentFeed(new Long(1), 0, 20))
                .thenReturn(new CombinedDTO(userDTO, tweetDTO));

        //mockMvc.perform(
          //      get("/feed/recent/").with(user(userDetails))
        //).andExpect()
    }


}

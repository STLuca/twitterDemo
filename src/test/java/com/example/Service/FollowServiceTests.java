package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.Follow;
import com.example.Entities.User;
import com.example.dao.JpaUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class FollowServiceTests {

    @Mock
    private JpaUserRepository userRepository;

    private FollowService followService = new FollowServiceImp(userRepository);

    private List<User> testUsers = new ArrayList<>();
    private List<UserDTO> testUsersDTOs = new ArrayList<>();
    private CombinedDTO dtoContainer;

    @Before
    public void init(){

        testUsers.add(spy(new User("bob", "bob@gmail.com")));
        testUsers.add(spy(new User("adam", "adam@gmail.com")));
        testUsers.add(spy(new User("susan", "susan@gmail.com")));

        when(testUsers.get(0).getId()).thenReturn(new Long(1));
        when(testUsers.get(1).getId()).thenReturn(new Long(2));

        testUsersDTOs.add(new UserDTO(new Long(1), "bob", 5, 2, 2));
        testUsersDTOs.add(new UserDTO(new Long(2), "adam", 5, 2, 2));

        dtoContainer = CombinedDTO.createFromUsers(testUsersDTOs);
    }

    @Test
    public void testFollowUser(){
        User follower = testUsers.get(0);
        User followee = testUsers.get(1);

        when(follower.getId()).thenReturn(new Long(1));
        when(followee.getId()).thenReturn(new Long(2));
        Follow follow = new Follow(followee, follower);


        when(userRepository.findUserByID(follower.getId())).thenReturn(follower);
        when(userRepository.findByUsername(followee.getUsername()))
                .thenReturn(followee);

        followService.followUser(follower.getId(), followee.getUsername());

        assertTrue(follower.getFollowing().contains(follow));
        assertTrue(followee.getFollowers().contains(follow));
    }

    @Test
    public void testUnfollowUser(){
        User follower = testUsers.get(0);
        User followee = testUsers.get(1);


        Follow follow = new Follow(followee, follower);
        follower.followUser(followee);

        when(userRepository.findUserByID(follower.getId())).thenReturn(follower);
        when(userRepository.findByUsername(followee.getUsername()))
                .thenReturn(followee);

        followService.unfollowUser(follower.getId(), followee.getUsername());

        assertTrue(follower.getFollowing().isEmpty());
        assertTrue(followee.getFollowers().isEmpty());
    }

    @Test
    public void testGetFollowing(){

        String testUsername = "susan";
        boolean old = true;
        int page = 0;
        int count = 20;

        when(followService.getFollowing(testUsername, old, page, count))
                .thenReturn(dtoContainer);
        List<UserDTO> users = followService.getFollowing(testUsername, old, page, count).getUsers();
        assertEquals(testUsersDTOs, users);

    }

    @Test
    public void testGetFollowers(){

        String testUsername = "susan";
        boolean old = true;
        int page = 0;
        int count = 20;

        when(followService.getFollowers(testUsername, old, page, count))
                .thenReturn(dtoContainer);
        List<UserDTO> users = followService.getFollowers(testUsername, old, page, count).getUsers();
        assertEquals(testUsersDTOs, users);

    }

}

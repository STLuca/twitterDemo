package com.example.dao;

import com.example.DataTransfer.UserDTO;
import com.example.Entities.User;
import com.example.configuration.PersistenceJPAConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {PersistenceJPAConfig.class, UserRepositoryImp.class})
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class UserRepositoryQueryTests {

    private static final String USER_ONE_USERNAME = "bob";
    private static final String USER_ONE_EMAIL = "bob@gmail.com";
    private static final String USER_ONE_PASSWORD = "cats123";

    private static final String USER_TWO_USERNAME = "susan";
    private static final String USER_TWO_EMAIL = "susan@gmail.com";
    private static final String USER_TWO_PASSWORD = "dogs789";

    private static final String USER_THREE_USERNAME = "adam";
    private static final String USER_THREE_EMAIL = "adam@gmail.com";
    private static final String USER_THREE_PASSWORD = "bunnies123";

    private static final String INVALID_USERNAME = "John";
    private static final Long INVALID_ID = new Long(6789);

    private User userOne;
    private User userTwo;
    private User userThree;

    private UserDTO userDTOOne;
    private UserDTO userDTOTwo;
    private UserDTO userDTOThree;

    @Autowired
    private JpaUserRepository userRepository;
    /*
    @Before
    public void init(){

        initUsers();
        initFollows();
        initDTOs();

    }

    private void initUsers(){
        userOne = new User(USER_ONE_USERNAME, USER_ONE_EMAIL);
        userTwo = new User(USER_TWO_USERNAME, USER_TWO_EMAIL);
        userThree = new User(USER_THREE_USERNAME, USER_THREE_EMAIL);

        userOne.setPassword(USER_ONE_PASSWORD);
        userTwo.setPassword(USER_TWO_PASSWORD);
        userThree.setPassword(USER_THREE_PASSWORD);

        userRepository.save(userOne);
        userRepository.save(userTwo);
        userRepository.save(userThree);
    }

    private void initFollows(){

        userOne.followUser(userTwo);
        waitXMilliseconds(1000);
        userOne.followUser(userThree);
        waitXMilliseconds(1000);
        userTwo.followUser(userOne);
        waitXMilliseconds(1000);
        userThree.followUser(userOne);
        waitXMilliseconds(1000);

    }

    private void waitXMilliseconds(int x){
        try {
            TimeUnit.MILLISECONDS.sleep(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /*
    private void initDTOs(){
        userDTOOne = new UserDTO(userOne.getId(), USER_ONE_USERNAME, 0, 2, 2);
        userDTOTwo = new UserDTO(userTwo.getId(), USER_TWO_USERNAME, 0, 1, 1);
        userDTOThree = new UserDTO(userThree.getId(), USER_THREE_USERNAME, 0, 1, 1);
    }

    @Test
    public void testgetUserByUsernameWithValidUsername(){

        List<UserDTO> user = userRepository.getUserByUsername(USER_ONE_USERNAME);
        assertEquals(user.size(), 1);
        assertEquals(user.get(0), userDTOOne);

    }

    @Test
    public void testGetUserByUsernameWithInvalidUsername(){
        List<UserDTO> user = userRepository.getUserByUsername(INVALID_USERNAME);
        assertEquals(user.size(), 0);
    }

    @Test
    public void testGetUsersByIDWithValidIDs(){
        Set<Long> ids = new HashSet<>();
        ids.add(userOne.getId());
        ids.add(userTwo.getId());
        ids.add(userThree.getId());

        List<UserDTO> users = userRepository.getUsersByIDs(ids);
        assertEquals(users.size(), 3);
        assertEquals(users.get(0), userDTOOne);
        assertEquals(users.get(1), userDTOTwo);
        assertEquals(users.get(2), userDTOThree);
    }

    @Test
    public void testGetUsersByIDWithInvalidIDs(){
        Set<Long> ids = new HashSet<>();
        ids.add(userOne.getId());
        ids.add(new Long(1451));
        ids.add(new Long(0));

        List<UserDTO> users = userRepository.getUsersByIDs(ids);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0), userDTOOne);
    }

    //GET FOLLOWERS BY USERNAME TESTS

    @Test(expected = NoResultException.class)
    public void testGetFollowersWithInvalidUsername(){
        List<UserDTO> users = userRepository.getFollowersByUsername(INVALID_USERNAME,true,  0, 20);
        assertEquals(users.size(), 0);
    }

    @Test
    public void testGetOldFollowersByUsernameWithValidUsername(){

        List<UserDTO> users = userRepository.getFollowersByUsername(USER_ONE_USERNAME,true,  0, 20);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), userDTOTwo);
        assertEquals(users.get(1), userDTOThree);

    }

    @Test
    public void testGetNewFollowersByUsernameWithValidUsername(){

        List<UserDTO> users = userRepository.getFollowersByUsername(USER_ONE_USERNAME,false,  0, 20);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), userDTOThree);
        assertEquals(users.get(1), userDTOTwo);

    }

    @Test
    public void testGetNewFollowersPaging(){
        List<UserDTO> users = userRepository.getFollowersByUsername(USER_ONE_USERNAME,false,  0, 1);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0), userDTOThree);

        List<UserDTO> newUsers = userRepository.getFollowersByUsername(USER_ONE_USERNAME,false,  1, 1);
        assertEquals(newUsers.size(), 1);
        assertEquals(newUsers.get(0), userDTOTwo);
    }

    //GET FOLLOWING BY USERNAME TESTS

    @Test(expected = NoResultException.class)
    public void testGetFollowingWithInvalidUsername(){
        List<UserDTO> users = userRepository.getFollowingByUsername(INVALID_USERNAME,true,  0, 20);
        assertEquals(users.size(), 0);
    }

    @Test
    public void testGetOldFollowingByUsernameWithValidUsername(){

        List<UserDTO> users = userRepository.getFollowingByUsername(USER_ONE_USERNAME,true,  0, 20);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), userDTOTwo);
        assertEquals(users.get(1), userDTOThree);

    }

    @Test
    public void testGetNewFollowingByUsernameWithValidUsername(){

        List<UserDTO> users = userRepository.getFollowingByUsername(USER_ONE_USERNAME,false,  0, 20);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), userDTOThree);
        assertEquals(users.get(1), userDTOTwo);

    }

    @Test
    public void testGetNewFollowingPaging(){

        List<UserDTO> users = userRepository.getFollowingByUsername(USER_ONE_USERNAME,false,  0, 1);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0), userDTOThree);

        List<UserDTO> newUsers = userRepository.getFollowingByUsername(USER_ONE_USERNAME,false,  1, 1);
        assertEquals(newUsers.size(), 1);
        assertEquals(newUsers.get(0), userDTOTwo);

    }

    //GET FOLLOWERS BY ID TESTS

    @Test
    public void testGetFollowersWithInvalidID(){
        List<UserDTO> users = userRepository.getFollowersByID(INVALID_ID,true,  0, 20);
        assertEquals(users.size(), 0);
    }

    @Test
    public void testGetOldFollowersByIDWithValidID(){

        List<UserDTO> users = userRepository.getFollowersByID(userOne.getId(),true,  0, 20);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), userDTOTwo);
        assertEquals(users.get(1), userDTOThree);

    }

    @Test
    public void testGetNewFollowersByUsernameWithValidID(){

        List<UserDTO> users = userRepository.getFollowersByID(userOne.getId(),false,  0, 2);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), userDTOThree);
        assertEquals(users.get(1), userDTOTwo);

    }



    @Test
    public void testGetNewFollowersByIDPaging(){
        List<UserDTO> users = userRepository.getFollowersByID(userOne.getId(),false,  0, 1);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0), userDTOThree);

        List<UserDTO> newUsers = userRepository.getFollowersByID(userOne.getId(),false,  1, 1);
        assertEquals(newUsers.size(), 1);
        assertEquals(newUsers.get(0), userDTOTwo);
    }

    //GET FOLLOWING BY ID TESTS

    @Test(expected = NoResultException.class)
    public void testGetFollowingWithInvalidID(){
        List<UserDTO> users = userRepository.getFollowingByID(INVALID_ID,true,  0, 20);
        assertEquals(users.size(), 0);
    }

    @Test
    public void testGetOldFollowingByIDWithValidID(){

        List<UserDTO> users = userRepository.getFollowingByID(userOne.getId(),true,  0, 20);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), userDTOTwo);
        assertEquals(users.get(1), userDTOThree);

    }

    @Test
    public void testGetNewFollowingByIDWithValidID(){

        List<UserDTO> users = userRepository.getFollowingByID(userOne.getId(),false,  0, 20);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), userDTOThree);
        assertEquals(users.get(1), userDTOTwo);

    }

    @Test
    public void testGetNewFollowingByIDUsingPaging(){

        List<UserDTO> users = userRepository.getFollowingByID(userOne.getId(),false,  0, 1);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0), userDTOThree);

        List<UserDTO> newUsers = userRepository.getFollowingByID(userOne.getId(),false,  1, 1);
        assertEquals(newUsers.size(), 1);
        assertEquals(newUsers.get(0), userDTOTwo);

    }

    */
}

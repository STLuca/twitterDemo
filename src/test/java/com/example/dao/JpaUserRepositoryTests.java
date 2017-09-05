package com.example.dao;

import com.example.Entities.User;
import com.example.configuration.PersistenceJPAConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import java.io.Console;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(classes = {PersistenceJPAConfig.class, UserRepositoryImp.class})
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaUserRepositoryTests {

    private static final String EXISTING_USER_USERNAME = "bob";
    private static final String EXISTING_USER_EMAIL = "bob@gmail.com";
    private static final String EXISTING_USER_PASSWORD = "cats123";

    private static final String NEW_USER_USERNAME = "susan";
    private static final String NEW_USER_EMAIL = "susan@gmail.com";
    private static final String NEW_USER_PASSWORD = "dogs789";



    @Autowired
    private JpaUserRepository userRepository;

    private User existingUser;
    private User newUser;

    @Before
    public void init(){
        existingUser = new User(EXISTING_USER_USERNAME, EXISTING_USER_EMAIL);
        existingUser.setPassword(EXISTING_USER_PASSWORD);
        newUser = new User(NEW_USER_USERNAME, NEW_USER_EMAIL);
        newUser.setPassword(NEW_USER_PASSWORD);

        userRepository.save(existingUser);
    }


    @Test
    public void testFindById(){
        assertEquals(userRepository.findById(existingUser.getId()).getUsername(), EXISTING_USER_USERNAME);
    }


    public void testFindByIdInvalidId(){
        User user = userRepository.findById(new Long(100));
        assertTrue(user == null);
    }

    @Test
    public void testFindByUsername(){
        assertEquals(userRepository.findByUsername(EXISTING_USER_USERNAME).getEmail(), EXISTING_USER_EMAIL);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testFindByUsernameInvalidUsername(){
        User user = userRepository.findByUsername(NEW_USER_USERNAME);
    }

    @Test
    public void testSave(){
        userRepository.save(newUser);
        assertEquals(userRepository.findByUsername(NEW_USER_USERNAME), newUser);
    }


    @Test(expected = EntityExistsException.class)
    public void testSaveWithDuplicateUsername(){
        //i think this is still in the persistence context
        //may need to create a new user with same id, maybe same username?
        User duplicateUsername = new User(EXISTING_USER_USERNAME, EXISTING_USER_EMAIL);
        duplicateUsername.setPassword(EXISTING_USER_PASSWORD);

        userRepository.save(existingUser);
    }

    @Test
    public void testUpdate(){
        final String updatedEmail = "Bobby@gmail.com";
        existingUser.setEmail(updatedEmail);
        userRepository.update(existingUser);
        assertEquals(userRepository.findById(existingUser.getId()).getEmail(), updatedEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNonSavedEntity(){
        userRepository.update(newUser);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testRemove(){
        userRepository.removeByUser(existingUser);
        userRepository.findByUsername(EXISTING_USER_USERNAME);
    }

    @Test
    public void testRemoveWithNonExistingUser(){
        userRepository.removeByUser(newUser);
    }


}

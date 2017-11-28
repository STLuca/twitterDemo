package com.example.dao;

import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.configuration.PersistenceJPAConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {PersistenceJPAConfig.class, TweetRepositoryImp.class, UserRepositoryImp.class})
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaTweetRepositoryTests {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaTweetRepository tweetRepository;

    private static final String EXISTING_USER_USERNAME = "bob";
    private static final String EXISTING_USER_EMAIL = "bob@gmail.com";
    private static final String EXISTING_USER_PASSWORD = "cats123";

    private static final String EXISTING_TWEET_MESSAGE = "Hi, I'm Bob tweeting for the first time!";
    private static final String NEW_TWEET_MESSAGE = "Hi, I'm Bob tweeting for the second time!";
    //private static final String NEW_REPLY_MESSAGE = "Hi, I'm Bob reply to my second tweet!";


    private User testUser;
    private Tweet existingTweet;
    private Tweet newTweet;

    @Before
    public void init(){
        testUser = new User(EXISTING_USER_USERNAME, EXISTING_USER_EMAIL);
        testUser.setPassword(EXISTING_USER_PASSWORD);
        userRepository.save(testUser);

        existingTweet = new Tweet(testUser, EXISTING_TWEET_MESSAGE);
        newTweet = new Tweet(testUser, NEW_TWEET_MESSAGE);
        tweetRepository.save(existingTweet);
    }

    @Test
    public void testFind(){
      //  assertEquals(tweetRepository.find(existingTweet.getId()).getMessage(), EXISTING_TWEET_MESSAGE);
    }

    @Test
    public void testFindWithInvalidID(){
     //   tweetRepository.find(new Long(110));
    }

    @Test
    public void testSave(){
        tweetRepository.save(newTweet);
       // assertEquals(tweetRepository.find(newTweet.getId()), newTweet);
    }

    @Test
    public void testDelete(){
        tweetRepository.delete(existingTweet);
       // assertEquals(tweetRepository.find(existingTweet.getId()), null);
    }

    @Test
    public void testDeleteWithNonExistingTweet(){
        tweetRepository.delete(newTweet);
    }


}

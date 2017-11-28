package com.example.dao;

import com.example.DataTransfer.TweetDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;
import com.example.configuration.PersistenceJPAConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {PersistenceJPAConfig.class, TweetRepositoryImp.class, UserRepositoryImp.class})
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class TweetRepositoryQueryTests {

    private static final Long DAY_IN_MS = new Long(1000 * 60 * 60 * 24);

    private static final String USER_ONE_USERNAME = "bob";
    private static final String USER_ONE_EMAIL = "bob@gmail.com";
    private static final String USER_ONE_PASSWORD = "cats123";

    private static final String USER_TWO_USERNAME = "susan";
    private static final String USER_TWO_EMAIL = "susan@gmail.com";
    private static final String USER_TWO_PASSWORD = "dogs789";

    private static final String USER_THREE_USERNAME = "adam";
    private static final String USER_THREE_EMAIL = "adam@gmail.com";
    private static final String USER_THREE_PASSWORD = "bunnies123";

    private static final Long INVALID_TWEETID = new Long(9999);

    private static final Date VALID_DATE = new Date(System.currentTimeMillis() - (1 * DAY_IN_MS));
    private static final Date INVALID_DATE = new Date(System.currentTimeMillis() + (1 * DAY_IN_MS));

    private User userOne;
    private User userTwo;
    private User userThree;

    private List<Tweet> tweets = new ArrayList<>();
    private List<TweetDTO> tweetDTOs = new ArrayList<>();

    @Autowired
    private JpaTweetRepository tweetRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Before
    public void init(){
        initUsers();
        initTweets();
        initLikes();
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

    private void initTweets(){
        tweets.add(new Tweet(userOne, "a"));
        tweetRepository.save(tweets.get(0));
        waitXMilliseconds(1000);
        tweets.add(Tweet.createReplyTweet(userTwo, "a", tweets.get(0)));
        tweetRepository.save(tweets.get(1));
        waitXMilliseconds(1000);
        tweets.add(Tweet.createReplyTweet(userThree, "a", tweets.get(0)));
        tweetRepository.save(tweets.get(2));
        waitXMilliseconds(1000);
        tweets.add(Tweet.createReplyTweet(userOne, "a", tweets.get(1)));
        tweetRepository.save(tweets.get(3));
        waitXMilliseconds(1000);
    }

    private void initLikes(){
        userOne.likeTweet(tweets.get(1));
        waitXMilliseconds(1000);
        userOne.likeTweet(tweets.get(3));
        waitXMilliseconds(1000);
        userTwo.likeTweet(tweets.get(0));
        waitXMilliseconds(1000);
        userTwo.likeTweet(tweets.get(3));
        waitXMilliseconds(1000);
        userThree.likeTweet(tweets.get(1));
        waitXMilliseconds(1000);
        userThree.likeTweet(tweets.get(3));
        waitXMilliseconds(1000);
    }

    private void initDTOs(){
        tweetDTOs.add(new TweetDTO(tweets.get(0).getId(), userOne.getId(), "a", tweets.get(0).getCreationTimestamp(), null, 1, 2));
        tweetDTOs.add(new TweetDTO(tweets.get(1).getId(), userTwo.getId(), "a", tweets.get(1).getCreationTimestamp(), tweets.get(0).getId(), 2, 1));
        tweetDTOs.add(new TweetDTO(tweets.get(2).getId(), userThree.getId(), "a", tweets.get(2).getCreationTimestamp(), tweets.get(0).getId(), 0, 0));
        tweetDTOs.add(new TweetDTO(tweets.get(3).getId(), userOne.getId(), "a", tweets.get(3).getCreationTimestamp(), tweets.get(1).getId(), 3, 0));

        for (Tweet tweet :  tweets){
            tweetRepository.update(tweet);
        }
    }

    private void waitXMilliseconds(int x){
        try {
            TimeUnit.MILLISECONDS.sleep(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTweetWithValidTweetID(){
        int x = 0;
        List<TweetDTO> tweet = tweetRepository.getTweet(tweets.get(x).getId());
        assertEquals(tweet.size(), 1);
        assertEquals(tweet.get(0), tweetDTOs.get(x));
    }

    @Test
    public void testGetTweetWithInvalidTweetID(){
        List<TweetDTO> tweet = tweetRepository.getTweet(INVALID_TWEETID);
        assertEquals(tweet.size(), 0);
    }

    //Test Tweets By Users
    //TEST WITH VALID IDS

    @Test
    public void testGetRecentTweetsByUsersWithAllValidIDs(){
        List<Long> ids = new ArrayList<>();
        ids.add(userOne.getId());
        ids.add(userThree.getId());

        List<TweetDTO> recentTweets = tweetRepository.getRecentTweetsByUsers(ids, false, 0, 20);
        assertEquals(recentTweets.size(), 3);
        assertEquals(recentTweets.get(0), tweetDTOs.get(3));
        assertEquals(recentTweets.get(1), tweetDTOs.get(2));
        assertEquals(recentTweets.get(2), tweetDTOs.get(0));
    }

    @Test
    public void testGetMostLikedTweetsByUsersWithValidIDs(){
        List<Long> ids = new ArrayList<>();
        ids.add(userOne.getId());
        ids.add(userTwo.getId());

        List<TweetDTO> recentTweets = tweetRepository.getLikedTweetsByUsers(ids, false, VALID_DATE, 0, 20);
        assertEquals(recentTweets.size(), 3);
        assertEquals(recentTweets.get(0), tweetDTOs.get(3));
        assertEquals(recentTweets.get(1), tweetDTOs.get(1));
        assertEquals(recentTweets.get(2), tweetDTOs.get(0));
    }

    @Test
    public void testGetMostRepliedTweetsByUsersWithValidIDs(){
        List<Long> ids = new ArrayList<>();
        ids.add(userOne.getId());
        ids.add(userTwo.getId());

        List<TweetDTO> recentTweets = tweetRepository.getRepliedTweetsByUsers(ids, false, VALID_DATE, 0, 20);
        assertEquals(recentTweets.size(), 3);
        assertEquals(recentTweets.get(0), tweetDTOs.get(0));
        assertEquals(recentTweets.get(1), tweetDTOs.get(1));
        assertEquals(recentTweets.get(2), tweetDTOs.get(3));
    }

    //TEST WITH VALID AND INVALID IDS

    @Test
    public void testGetRecentTweetsByUsersWithTwoValidTwoInvalidIDs(){
        List<Long> ids = new ArrayList<>();
        ids.add(userTwo.getId());
        ids.add(userThree.getId());
        ids.add(new Long(8878));
        ids.add(new Long(9979));

        List<TweetDTO> recentTweets = tweetRepository.getRecentTweetsByUsers(ids, false, 0, 20);
        assertEquals(recentTweets.size(), 2);
        assertEquals(recentTweets.get(0), tweetDTOs.get(2));
        assertEquals(recentTweets.get(1), tweetDTOs.get(1));
    }

    @Test
    public void testGetMostLikedTweetsByUsersWithTwoValidTwoInvalidIDs(){
        List<Long> ids = new ArrayList<>();
        ids.add(userTwo.getId());
        ids.add(userThree.getId());
        ids.add(new Long(8878));
        ids.add(new Long(9979));

        List<TweetDTO> recentTweets = tweetRepository.getLikedTweetsByUsers(ids, false, VALID_DATE,  0, 20);
        assertEquals(recentTweets.size(), 2);
        assertEquals(recentTweets.get(0), tweetDTOs.get(1));
        assertEquals(recentTweets.get(1), tweetDTOs.get(2));
    }

    @Test
    public void testGetMostRepliedTweetsByUsersWithTwoValidTwoInvalidIDs(){
        List<Long> ids = new ArrayList<>();
        ids.add(userTwo.getId());
        ids.add(userThree.getId());
        ids.add(new Long(8878));
        ids.add(new Long(9979));

        List<TweetDTO> recentTweets = tweetRepository.getRepliedTweetsByUsers(ids, false, VALID_DATE, 0, 20);
        assertEquals(recentTweets.size(), 2);
        assertEquals(recentTweets.get(0), tweetDTOs.get(1));
        assertEquals(recentTweets.get(1), tweetDTOs.get(2));
    }

    //TEST WITH INVALID IDS

    @Test
    public void testGetRecentTweetsByUsersWithInvalidIDs(){
        List<Long> ids = new ArrayList<>();
        ids.add(new Long(8878));
        ids.add(new Long(9979));

        List<TweetDTO> recentTweets = tweetRepository.getRecentTweetsByUsers(ids, false, 0, 20);
        assertEquals(recentTweets.size(), 0);
    }

    @Test
    public void testGetMostLikedTweetsByUsersWithInvalidIDs(){
        List<Long> ids = new ArrayList<>();
        ids.add(new Long(8878));
        ids.add(new Long(9979));

        List<TweetDTO> recentTweets = tweetRepository.getLikedTweetsByUsers(ids, false, VALID_DATE,0, 20);
        assertEquals(recentTweets.size(), 0);
    }

    @Test
    public void testGetMostRepliedTweetsByUsersWithInvalidIDs(){
        List<Long> ids = new ArrayList<>();
        ids.add(new Long(8878));
        ids.add(new Long(9979));

        List<TweetDTO> recentTweets = tweetRepository.getRepliedTweetsByUsers(ids, false, VALID_DATE, 0, 20);
        assertEquals(recentTweets.size(), 0);
    }

    //TEST WITH INVALID DATES

    @Test
    public void testGetMostLikedTweetsWithInvalidDate(){
        List<Long> ids = new ArrayList<>();
        ids.add(userTwo.getId());
        ids.add(userThree.getId());

        List<TweetDTO> responseDTOs = tweetRepository.getLikedTweetsByUsers(ids, false, INVALID_DATE, 0, 20);
        assertEquals(responseDTOs.size(), 0);
    }

    @Test
    public void testGetMostRepliedTweetsWithInvalidDate(){
        List<Long> ids = new ArrayList<>();
        ids.add(userTwo.getId());
        ids.add(userThree.getId());

        List<TweetDTO> responseDTOs = tweetRepository.getRepliedTweetsByUsers(ids, false, INVALID_DATE, 0, 20);
        assertEquals(responseDTOs.size(), 0);
    }

    //TEST WITH DATE FILTERING OUT RESULT
    @Test
    public void testGetMostLikedTweetsWithResultFilteredUsingTimestamp(){
        List<Long> ids = new ArrayList<>();
        ids.add(userOne.getId());
        ids.add(userTwo.getId());
        ids.add(userThree.getId());

        Date date = new Date(tweets.get(0).getCreationTimestamp().getTime() + 500);

        List<TweetDTO> responseDTOs = tweetRepository.getLikedTweetsByUsers(ids, false, date, 0, 20);
        assertEquals(responseDTOs.size(), 3);
        assertEquals(responseDTOs.get(0), tweetDTOs.get(3));
        assertEquals(responseDTOs.get(1), tweetDTOs.get(1));
        assertEquals(responseDTOs.get(2), tweetDTOs.get(2));
    }

    @Test
    public void testGetMostRepliedTweetsWithResultFilteredUsingTimestamp(){
        List<Long> ids = new ArrayList<>();
        ids.add(userOne.getId());
        ids.add(userTwo.getId());

        Date date = new Date(tweets.get(0).getCreationTimestamp().getTime() + 500);

        List<TweetDTO> responseDTOs = tweetRepository.getRepliedTweetsByUsers(ids, false, date, 0, 20);
        assertEquals(responseDTOs.size(), 2);
        assertEquals(responseDTOs.get(0), tweetDTOs.get(1));
        assertEquals(responseDTOs.get(1), tweetDTOs.get(3));
    }

    //TEST GET USERS LIKES
    @Test
    public void testGetUsersRecentLikesWithTwoValidUsers(){
        List<Long> ids = new ArrayList<>();
        ids.add(userOne.getId());
        ids.add(userTwo.getId());

        List<TweetDTO> responseDTOs = tweetRepository.getUsersLikedTweets(ids, false, 0, 20);
        assertEquals(responseDTOs.size(), 3);
        assertEquals(responseDTOs.get(0), tweetDTOs.get(3));
        assertEquals(responseDTOs.get(1), tweetDTOs.get(0));
        assertEquals(responseDTOs.get(2), tweetDTOs.get(1));
    }

    @Test
    public void testGetUsersOldestLikesWithTwoValidUsers(){
        List<Long> ids = new ArrayList<>();
        ids.add(userOne.getId());
        ids.add(userTwo.getId());

        List<TweetDTO> responseDTOs = tweetRepository.getUsersLikedTweets(ids, true, 0, 20);
        assertEquals(responseDTOs.size(), 3);
        assertEquals(responseDTOs.get(0), tweetDTOs.get(1));
        assertEquals(responseDTOs.get(1), tweetDTOs.get(0));
        assertEquals(responseDTOs.get(2), tweetDTOs.get(3));
    }

    @Test
    public void testGetUsersRecentLikesWithOneValidUser(){
        List<Long> ids = new ArrayList<>();
        ids.add(userOne.getId());

        List<TweetDTO> responseDTOs = tweetRepository.getUsersLikedTweets(ids, false, 0, 20);
        assertEquals(responseDTOs.size(), 2);
        assertEquals(responseDTOs.get(0), tweetDTOs.get(3));
        assertEquals(responseDTOs.get(1), tweetDTOs.get(1));
    }

    @Test
    public void testGetUsersOldestLikesWithOneValidUser(){
        List<Long> ids = new ArrayList<>();
        ids.add(userOne.getId());

        List<TweetDTO> responseDTOs = tweetRepository.getUsersLikedTweets(ids, true, 0, 20);
        assertEquals(responseDTOs.size(), 2);
        assertEquals(responseDTOs.get(0), tweetDTOs.get(1));
        assertEquals(responseDTOs.get(1), tweetDTOs.get(3));
    }

    //TEST REPLIES TO TWEETS

    @Test
    public void testGetRecentRepliesWithValidTweet(){
        List<TweetDTO> replies = tweetRepository.getRecentTweetReplies(tweets.get(0).getId(), false, 0, 20);

        assertEquals(replies.size(), 2);
        assertEquals(replies.get(0), tweetDTOs.get(2));
        assertEquals(replies.get(1), tweetDTOs.get(1));

    }

    @Test
    public void testGetMostLikedRepliesWithValidTweet(){
        List<TweetDTO> replies = tweetRepository.getLikedTweetReplies(tweets.get(0).getId(), false, 0, 20);

        assertEquals(replies.size(), 2);
        assertEquals(replies.get(0), tweetDTOs.get(1));
        assertEquals(replies.get(1), tweetDTOs.get(2));

    }

    @Test
    public void testGetMostRepliedRepliesWithValidTweet(){
        List<TweetDTO> replies = tweetRepository.getRepliedTweetReplies(tweets.get(0).getId(), false, 0, 20);

        assertEquals(replies.size(), 2);
        assertEquals(replies.get(0), tweetDTOs.get(1));
        assertEquals(replies.get(1), tweetDTOs.get(2));

    }

    @Test
    public void testGetOldestRepliesWithValidTweet(){
        List<TweetDTO> replies = tweetRepository.getRecentTweetReplies(tweets.get(0).getId(), true, 0, 20);

        assertEquals(replies.size(), 2);
        assertEquals(replies.get(0), tweetDTOs.get(1));
        assertEquals(replies.get(1), tweetDTOs.get(2));

    }

    @Test
    public void testGetLeastLikedRepliesWithValidTweet(){
        List<TweetDTO> replies = tweetRepository.getLikedTweetReplies(tweets.get(0).getId(), true, 0, 20);

        assertEquals(replies.size(), 2);
        assertEquals(replies.get(0), tweetDTOs.get(2));
        assertEquals(replies.get(1), tweetDTOs.get(1));

    }

    @Test
    public void testGetLeastRepliedRepliesWithValidTweet(){
        List<TweetDTO> replies = tweetRepository.getRepliedTweetReplies(tweets.get(0).getId(), true, 0, 20);

        assertEquals(replies.size(), 2);
        assertEquals(replies.get(0), tweetDTOs.get(2));
        assertEquals(replies.get(1), tweetDTOs.get(1));

    }

    @Test
    public void testGetRecentRepliesWithInvalidTweet(){
        List<TweetDTO> replies = tweetRepository.getRecentTweetReplies(new Long(9999), false, 0, 20);
        assertEquals(replies.size(), 0);
    }

    @Test
    public void testGetLikedRepliesWithInvalidTweet(){
        List<TweetDTO> replies = tweetRepository.getLikedTweetReplies(new Long(9999), false, 0, 20);
        assertEquals(replies.size(), 0);
    }

    @Test
    public void testGetRepliedRepliesWithInvalidTweet(){
        List<TweetDTO> replies = tweetRepository.getRepliedTweetReplies(new Long(9999), false, 0, 20);
        assertEquals(replies.size(), 0);
    }


}

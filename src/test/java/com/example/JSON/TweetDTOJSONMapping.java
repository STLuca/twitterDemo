package com.example.JSON;

import com.example.DataTransfer.TweetDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class TweetDTOJSONMapping {

    @Autowired
    private JacksonTester<TweetDTO> json;

    private Long tweetID = new Long(2);
    private Long userID = new Long(1);
    private Long replyId = new Long(3);
    private String message = "tweet message";
    private Date timestamp = new Date();
    private int likes = 3;
    private int replies = 5;

    @Test
    public void testSerialize() throws Exception {
        TweetDTO tweet = new TweetDTO(tweetID, userID, message, timestamp, replyId, likes, replies);
        JsonContent<TweetDTO> jsonValue = this.json.write(tweet);
        assertThat(jsonValue).hasJsonPathNumberValue("@.id");
        assertThat(jsonValue).hasJsonPathNumberValue("@.userID");
        assertThat(jsonValue).hasJsonPathStringValue("@.message");
        assertThat(jsonValue).hasJsonPathNumberValue("@.timestamp");
        assertThat(jsonValue).hasJsonPathNumberValue("@.replyTo");
        assertThat(jsonValue).hasJsonPathNumberValue("@.numOfLikes");
        assertThat(jsonValue).hasJsonPathNumberValue("@.numOfReplies");

        assertThat(jsonValue).extractingJsonPathNumberValue("@.id").isEqualTo(tweetID.intValue());
        assertThat(jsonValue).extractingJsonPathNumberValue("@.userID").isEqualTo(userID.intValue());
        assertThat(jsonValue).extractingJsonPathStringValue("@.message").isEqualTo(message);
        assertThat(jsonValue).extractingJsonPathNumberValue("@.timestamp").isEqualTo(timestamp.getTime());
        assertThat(jsonValue).extractingJsonPathNumberValue("@.replyTo").isEqualTo(replyId.intValue());
        assertThat(jsonValue).extractingJsonPathNumberValue("@.numOfLikes").isEqualTo(likes);
        assertThat(jsonValue).extractingJsonPathNumberValue("@.numOfReplies").isEqualTo(replies);
    }

}

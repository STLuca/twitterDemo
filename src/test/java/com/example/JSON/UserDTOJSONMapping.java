package com.example.JSON;

import com.example.DataTransfer.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class UserDTOJSONMapping {

    @Autowired
    private JacksonTester<UserDTO> json;

    private Long id = new Long(1);
    private String username = "bob";
    private int numOfFollowers = 3;
    private int numOfFollowing = 5;
    private int numOfTweets = 10;
    private boolean iFollow = false;

    @Test
    public void testSerialize() throws Exception {
        UserDTO user = new UserDTO(id, username, numOfTweets, numOfFollowers, numOfFollowing, iFollow);
        JsonContent<UserDTO> jsonValue = this.json.write(user);
        assertThat(jsonValue).hasJsonPathNumberValue("@.id");
        assertThat(jsonValue).hasJsonPathStringValue("@.username");
        assertThat(jsonValue).hasJsonPathNumberValue("@.numOfTweets");
        assertThat(jsonValue).hasJsonPathNumberValue("@.numOfFollowers");
        assertThat(jsonValue).hasJsonPathNumberValue("@.numOfFollowing");
        assertThat(jsonValue).hasJsonPathBooleanValue("@.iFollowing");

        assertThat(jsonValue).extractingJsonPathNumberValue("@.id").isEqualTo(id.intValue());
        assertThat(jsonValue).extractingJsonPathStringValue("@.username").isEqualTo(username);
        assertThat(jsonValue).extractingJsonPathNumberValue("@.numOfTweets").isEqualTo(numOfTweets);
        assertThat(jsonValue).extractingJsonPathNumberValue("@.numOfFollowers").isEqualTo(numOfFollowers);
        assertThat(jsonValue).extractingJsonPathNumberValue("@.numOfFollowing").isEqualTo(numOfFollowing);
        assertThat(jsonValue).extractingJsonPathBooleanValue("@.iFollowing").isEqualTo(iFollow);
    }

}

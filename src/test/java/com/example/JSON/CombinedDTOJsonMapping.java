package com.example.JSON;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.TweetDTO;
import com.example.DataTransfer.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class CombinedDTOJsonMapping {

    @Autowired
    private JacksonTester<CombinedDTO> json;

    @Test
    public void testSerialize() throws Exception{
        List<UserDTO> users = Arrays.asList(new UserDTO(new Long(1), "bob", 1, 2, 3, false));
        List<TweetDTO> tweets = Arrays.asList(new TweetDTO(new Long(2), new Long(1), "tweet message", new Date(), null, 3, 3, false));
        CombinedDTO returnValue = new CombinedDTO(users, tweets);
        JsonContent<CombinedDTO> jsonValue = this.json.write(returnValue);
        assertThat(jsonValue).hasJsonPathArrayValue("@.users");
        assertThat(jsonValue).hasJsonPathArrayValue("@.tweets");

    }

}

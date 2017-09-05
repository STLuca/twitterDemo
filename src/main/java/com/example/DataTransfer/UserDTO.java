package com.example.DataTransfer;

import javax.persistence.SqlResultSetMapping;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class UserDTO {

    private final Long id;
    private final String username;
    private final int numOfTweets;
    private final int numOfFollowers;
    private final int numOfFollowing;


    public UserDTO(Long id, String username, int tweets, int followers,  int following) {
        this.id = id;
        this.username = username;
        this.numOfTweets = tweets;
        this.numOfFollowers = followers;
        this.numOfFollowing = following;

    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getNumOfTweets() {
        return numOfTweets;
    }

    public int getNumOfFollowers() {
        return numOfFollowers;
    }

    public int getNumOfFollowing() {
        return numOfFollowing;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){return true;}
        if (!(o instanceof UserDTO)){return false;}
        UserDTO user = (UserDTO) o;
        return user.id.equals(this.id) &&
                user.username.equals(this.username) &&
                user.numOfTweets == this.numOfTweets &&
                user.numOfFollowing == this.numOfFollowing &&
                user.numOfFollowers == this.numOfFollowers;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + (numOfTweets + numOfFollowers + numOfFollowing);
        return result;
    }
}

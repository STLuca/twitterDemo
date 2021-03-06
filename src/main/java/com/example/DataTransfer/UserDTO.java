package com.example.DataTransfer;


public class UserDTO {

    private final Long id;
    private final String username;
    private final int numOfTweets;
    private final int numOfFollowers;
    private final int numOfFollowing;
    private final boolean iFollowing;

    public UserDTO(Long id, String username, int tweets, int followers,  int following, boolean IFollowing) {
        this.id = id;
        this.username = username;
        this.numOfTweets = tweets;
        this.numOfFollowers = followers;
        this.numOfFollowing = following;
        this.iFollowing = IFollowing;
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

    public boolean getiFollowing() { return iFollowing; }

    @Override
    public boolean equals(Object o) {
        if (o == this){return true;}
        if (!(o instanceof UserDTO)){return false;}
        UserDTO user = (UserDTO) o;
        return user.id.equals(this.id) &&
                user.username.equals(this.username) &&
                user.numOfTweets == this.numOfTweets &&
                user.numOfFollowing == this.numOfFollowing &&
                user.numOfFollowers == this.numOfFollowers &&
                user.iFollowing == this.iFollowing;
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

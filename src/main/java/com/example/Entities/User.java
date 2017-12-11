package com.example.Entities;



import com.example.DataTransfer.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SqlResultSetMapping(name = "UserDTOMapping", classes = {
        @ConstructorResult(targetClass = UserDTO.class,
                columns = {
                        @ColumnResult(name = "id", type=Long.class),
                        @ColumnResult(name = "username", type = String.class),
                        @ColumnResult(name = "tweets", type=Integer.class),
                        @ColumnResult(name = "followers", type=Integer.class),
                        @ColumnResult(name = "following", type=Integer.class),
                        @ColumnResult(name = "IFollow", type=boolean.class)
                })
})
@Entity(name = "User")
@Table(name = "Users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @OneToMany(orphanRemoval = true, mappedBy = "user")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Tweet> myTweets = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Follow> following = new HashSet<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "likedBy", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Like> likes = new HashSet<>();

    //constructors
    public User(){}
    public User(String username, String email) {

        this.username = username;
        this.email = email;

    }


    //helper methods
    public void addTweet(Tweet tweet){
        myTweets.add(tweet);
    }

    public void followUser(User user){
        Follow follow = new Follow(user, this);
        following.add(follow);
        user.followers.add(follow);
    }

    public void unfollowUser(User user){
        Follow follow = new Follow(user, this);
        following.remove(follow);
        user.followers.remove(follow);
    }

    public void likeTweet(Tweet tweet){
        Like like = new Like(this, tweet);
        likes.add(like);
    }

    public void unlikeTweet(Tweet tweet){
        Like like = new Like(this, tweet);
        if (likes.contains(like)){
            likes.remove(like);
        }
    }



    //getters and setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Tweet> getMyTweets() {
        return myTweets;
    }

    public void setMyTweets(List<Tweet> myTweets) {
        this.myTweets = myTweets;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Follow> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Follow> following) {
        this.following = following;
    }

    public Set<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Follow> followers) {
        this.followers = followers;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){return true;}
        if (!(o instanceof User)){return false;}
        User user = (User) o;
        return user.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString(){
        return "ID: " + id + "\n" +
                "Username: " + username + "\n" +
                "Email: " + email + "\n" +
                "password: " + password + "\n";
    }
}




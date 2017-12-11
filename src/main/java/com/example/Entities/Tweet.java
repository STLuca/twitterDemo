package com.example.Entities;

import com.example.DataTransfer.TweetDTO;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@SqlResultSetMapping(name = "TweetDTOMapping", classes = {
        @ConstructorResult(targetClass = TweetDTO.class,
                columns = {
                        @ColumnResult(name = "id", type=Long.class),
                        @ColumnResult(name = "userID", type = Long.class),
                        @ColumnResult(name = "message", type=String.class),
                        @ColumnResult(name = "creationTS", type=Date.class),
                        @ColumnResult(name = "replyTo", type=Long.class),
                        @ColumnResult(name = "likes", type=Integer.class),
                        @ColumnResult(name = "replies", type=Integer.class),
                        @ColumnResult(name = "ILiked", type=boolean.class)
                })
})
@Entity(name = "Tweet")
@Table(name = "Tweets")
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, updatable = false)
    private User user;

    @NotNull
    @Column(length = 160)
    protected String message;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "replies", joinColumns = @JoinColumn(name = "child_tweet"),
                                 inverseJoinColumns = @JoinColumn(name = "parent_tweet"))
    private Tweet repliedTo;

    //@JsonIgnore
    @OneToMany(mappedBy = "repliedTo", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Tweet> replies = new ArrayList<>();

    @OneToMany(mappedBy = "tweetLiked", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Like> likedBy = new HashSet<>();

    //constructors
    public Tweet(){}
    public Tweet(User user, String message){
        this.user = user;
        this.message = message;
        this.creationTimestamp = new Date();
    }

    public static Tweet createReplyTweet(User user, String message, Tweet replyToTweet){
        Tweet tweet = new Tweet(user, message);
        tweet.repliedTo = replyToTweet;
        replyToTweet.replies.add(tweet);
        return tweet;
    }

    /*helper methods
    public void addLike(User user){
        Like like = new Like(user, this);
        getLikedBy().add(like);
    }

    public void removeLike(User user){
        getLikedBy().remove(user);
    }*/

    //getters and setters
    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Tweet getRepliedTo() {
        return repliedTo;
    }

    public void setRepliedTo(Tweet repliedTo) {
        this.repliedTo = repliedTo;
    }

    public List<Tweet> getReplies() {
        return replies;
    }

    public void setReplies(List<Tweet> replies) {
        this.replies = replies;
    }

    public Set<Like> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Set<Like> likedBy) {
        this.likedBy = likedBy;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){return true;}
        if (!(o instanceof Tweet)){return false;}
        Tweet tweet = (Tweet) o;
        return tweet.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString(){
        return "ID: : " + id + "\n" +
                "User: " + user.getUsername() + "\n" +
                "message: " + message + "\n" +
                "creationTimestamp: " + creationTimestamp + "\n";
    }
}

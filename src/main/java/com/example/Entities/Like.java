package com.example.Entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(name = "Like")
@Table(name = "Likes")
public class Like {

    @Embeddable
    public static class Id implements Serializable {

        @Column(name = "user_id")
        protected Long likedBy;

        @Column(name = "tweet_id")
        protected Long tweet;

        public Id() {
        }

        public Id(Long likedBy, Long tweet) {
            this.likedBy = tweet;
            this.tweet = tweet;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.likedBy.equals(that.likedBy)
                        && this.tweet.equals(that.tweet);
            }
            return false;
        }

        public int hashCode() {
            return likedBy.hashCode() + tweet.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User likedBy;

    @ManyToOne
    @JoinColumn(name = "tweet_id", insertable = false, updatable = false)
    private Tweet tweetLiked;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date ts;

    public Like() {
    }

    public Like(User likedBy, Tweet tweetLiked) {
        this.likedBy = likedBy;
        this.tweetLiked = tweetLiked;
        this.ts = new Date();

        this.id.likedBy = likedBy.getId();
        this.id.tweet = tweetLiked.getId();

    }

    public User getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(User likedBy) {
        this.likedBy = likedBy;
    }

    public Tweet getTweetLiked() {
        return tweetLiked;
    }

    public void setTweetLiked(Tweet tweetLiked) {
        this.tweetLiked = tweetLiked;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Like that = (Like) o;
        return Objects.equals(likedBy, that.likedBy) &&
                Objects.equals(tweetLiked, that.tweetLiked);
    }

    @Override
    public int hashCode(){
        return Objects.hash(likedBy, tweetLiked);
    }
}

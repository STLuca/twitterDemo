package com.example.DataTransfer;

import java.util.Date;


public class TweetDTO {

    private final Long id;
    private final Long userID;
    private final String message;
    private final String timestamp;
    private final Long replyTo;
    private final int numOfLikes;
    private final int numOfReplies;
    private final boolean iLiked;

    public TweetDTO(Long id, Long userID, String message, Date timestamp, Long replyTo, int likes, int replies, boolean ILiked) {

        Long replyToID = replyTo == null ? 0 : replyTo;

        this.id = id;
        this.userID = userID;
        this.message = message;
        this.timestamp = timestamp.toString();
        this.replyTo = replyToID;
        this.numOfLikes = likes;
        this.numOfReplies = replies;
        this.iLiked = ILiked;
    }

    public Long getId() {
        return id;
    }

    public Long getUserID() {
        return userID;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public int getNumOfReplies() {
        return numOfReplies;
    }

    public boolean getiLiked(){
        return iLiked;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){return true;}
        if (!(o instanceof TweetDTO)){return false;}
        TweetDTO tweet = (TweetDTO) o;
        return tweet.id.equals(this.id) &&
                tweet.userID.equals(this.userID) &&
                tweet.message.equals(this.message) &&
                //tweet.timestamp.compareTo(this.timestamp) == 0 &&
                tweet.replyTo.equals(this.replyTo) &&
                tweet.numOfLikes == numOfLikes &&
                tweet.numOfReplies == numOfReplies &&
                tweet.iLiked == iLiked;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + numOfLikes;
        result = 31 * result + numOfReplies;
        result = 31 * result + (id.hashCode() +
                                userID.hashCode() +
                                timestamp.hashCode() +
                                message.hashCode() +
                                replyTo.hashCode());
        return result;
    }
}

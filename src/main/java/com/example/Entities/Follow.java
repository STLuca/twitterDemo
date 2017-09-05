package com.example.Entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(name = "Follow")
@Table(name = "Follows")
public class Follow {

    @Embeddable
    public static class Id implements Serializable {

        @Column(name = "followee_id")
        protected Long followeeID;

        @Column(name = "follower_id")
        protected Long followerID;

        public Id() {
        }

        public Id(Long followeeID, Long followerID) {
            this.followeeID = followeeID;
            this.followerID = followerID;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.followeeID.equals(that.followeeID)
                        && this.followerID.equals(that.followerID);
            }
            return false;
        }

        public int hashCode() {
            return followeeID.hashCode() + followerID.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @ManyToOne
    @JoinColumn(name = "followee_id", insertable = false, updatable = false)
    User followee;

    @ManyToOne
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    User follower;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date ts;

    public Follow(){}
    public Follow(User followee, User follower) {
        this.followee = followee;
        this.follower = follower;
        this.ts = new Date();

        this.id.followeeID = followee.getId();
        this.id.followerID = follower.getId();

    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
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
        Follow that = (Follow) o;
        return Objects.equals(followee, that.followee) &&
                Objects.equals(follower, that.follower);
    }

    @Override
    public int hashCode(){
        return Objects.hash(followee, follower);
    }
}

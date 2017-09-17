package com.example.dao;

import com.example.DataTransfer.TweetDTO;
import com.example.Entities.Tweet;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Repository
public class TweetRepositoryImp implements JpaTweetRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Tweet getReference(Long id){
        return em.getReference(Tweet.class, id);
    }

    @Override
    public Tweet find(Long id) {
        return em.find(Tweet.class, id);
    }

    @Override
    public void save(Tweet tweet) {
        em.persist(tweet);
    }

    @Override
    public void update(Tweet tweet) {
        em.merge(tweet);
    }

    @Override
    public void delete(Tweet tweet) {
        em.remove(tweet);
    }

    /*@Override
    public List<TweetDTO> getTweet(Long tweetID) {
        String qs = "\n" +
                "SELECT     t.id                            ID,                         \n" +
                "           t.user_id                       userID,                     \n" +
                "           t.message                       message,                    \n" +
                "           t.creationTimestamp             creationTS,                 \n" +
                "           rt.parent_tweet                 replyTo,                    \n" +
                "           COUNT(DISTINCT r.child_tweet)   replies,                    \n" +
                "           COUNT(DISTINCT l.user_id)       likes                       \n" +
                "   FROM tweets t                                                       \n" +
                "       LEFT JOIN replies r     ON t.id = r.parent_tweet                \n" +
                "       LEFT JOIN replies rt    ON t.id = rt.child_tweet                \n" +
                "       LEFT JOIN likes   l     ON t.id = l.tweet_id                    \n" +
                "   WHERE   t.id = :tweetID                                             \n" +
                "   GROUP BY t.id                                                       \n";
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("tweetID", tweetID);
        return query.getResultList();

    }*/

    @Override
    public List<TweetDTO> getTweet(Long tweetID) {
        String qs = "\n" +
                "SELECT * from tweetSummary t            \n" +
                "   WHERE   t.id = :tweetID             \n";
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("tweetID", tweetID);
        return query.getResultList();

    }

    @Override
    public List<TweetDTO> getRecentTweetsByUsers(List<Long> userIDs, int page, int count) {
        String qs = "\n" +
                "SELECT t.id                            ID,                         \n"+
                "       t.user_id                       userID,                     \n"+
                "       t.message                       message,                    \n"+
                "       t.creationTimestamp             creationTS,                 \n"+
                "       rt.parent_tweet                 replyTo,                    \n"+
                "       COUNT(DISTINCT r.child_tweet)   replies,                    \n"+
                "       COUNT(DISTINCT l.user_id)       likes                       \n"+
                "FROM             tweets t                                          \n"+
                "       LEFT JOIN replies r     ON t.id = r.parent_tweet            \n"+
                "       LEFT JOIN replies rt    ON t.id = rt.child_tweet            \n"+
                "       LEFT JOIN likes   l     ON t.id = l.tweet_id                \n"+
                "   WHERE t.user_id IN (:userIDs)                                   \n"+
                "   GROUP BY t.id                                                   \n"+
                "   ORDER BY t.creationTimestamp DESC                               \n";
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("userIDs", userIDs)
                .setFirstResult(page * count)
                .setMaxResults(count);
        return query.getResultList();
    }

    @Override
    public List<TweetDTO> getMostLikedTweetsByUsers(List<Long> userIDs, Date withinDate, int page, int count) {
        String qs = "\n" +
                "SELECT     t.id                            ID,                         \n"+
                "           t.user_id                       userID,                     \n"+
                "           t.message                       message,                    \n"+
                "           t.creationTimestamp             creationTS,                 \n"+
                "           rt.parent_tweet                 replyTo,                    \n"+
                "           COUNT(DISTINCT r.child_tweet)   replies,                    \n"+
                "           COUNT(DISTINCT l.user_id)       likes                       \n"+
                "   FROM tweets t                                                       \n"+
                "       LEFT JOIN replies r     ON t.id = r.parent_tweet                \n"+
                "       LEFT JOIN replies rt    ON t.id = rt.child_tweet                \n"+
                "       LEFT JOIN likes   l     ON t.id = l.tweet_id                    \n"+
                "   WHERE   t.user_id IN (:userIDs)   AND                               \n"+
                "           t.creationTimestamp > :date                                 \n"+
                "   GROUP BY t.id                                                       \n"+
                "   ORDER BY likes DESC                                                 \n";
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("userIDs", userIDs)
                .setParameter("date", withinDate)
                .setFirstResult(page * count)
                .setMaxResults(count);
        return query.getResultList();
    }

    @Override
    public List<TweetDTO> getMostRepliedTweetsByUsers(List<Long> userIDs, Date withinDate, int page, int count) {
        String qs = "\n" +
                "SELECT     t.id                            ID,                         \n"+
                "           t.user_id                       userID,                     \n"+
                "           t.message                       message,                    \n"+
                "           t.creationTimestamp             creationTS,                 \n"+
                "           rt.parent_tweet                 replyTo,                    \n"+
                "           COUNT(DISTINCT r.child_tweet)   replies,                    \n"+
                "           COUNT(DISTINCT l.user_id)       likes                       \n"+
                "   FROM tweets t                                                       \n"+
                "       LEFT JOIN replies r     ON t.id = r.parent_tweet                \n"+
                "       LEFT JOIN replies rt    ON t.id = rt.child_tweet                \n"+
                "       LEFT JOIN likes   l     ON t.id = l.tweet_id                    \n"+
                "   WHERE   t.user_id IN (:userIDs)   AND                               \n"+
                "           t.creationTimestamp > :date                                 \n"+
                "   GROUP BY t.id                                                       \n"+
                "   ORDER BY replies DESC                                               \n";

        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("userIDs", userIDs)
                .setParameter("date", withinDate)
                .setFirstResult(page * count)
                .setMaxResults(count);
        return query.getResultList();
    }

    @Override
    public List<TweetDTO> getUsersLikedTweets(List<Long> userIDs, boolean old, int page, int count) {
        String qs = "\n" +
                "SELECT     t.id                            ID,                         \n" +
                "           t.user_id                       userID,                     \n" +
                "           t.message                       message,                    \n" +
                "           t.creationTimestamp             creationTS,                 \n" +
                "           rt.parent_tweet                 replyTo,                    \n" +
                "           COUNT(DISTINCT r.child_tweet)   replies,                    \n" +
                "           COUNT(DISTINCT l.user_id)       likes                       \n" +
                "   FROM tweets t                                                       \n" +
                "       LEFT JOIN replies r     ON t.id = r.parent_tweet                \n" +
                "       LEFT JOIN replies rt    ON t.id = rt.child_tweet                \n" +
                "       LEFT JOIN likes   l     ON t.id = l.tweet_id                    \n" +
                "       JOIN      likes   ll    ON t.id = ll.tweet_id AND               \n" +
                "                                  ll.user_id in (:userIDs)             \n" +
                "   GROUP BY t.id                                                       \n" +
                "   ORDER BY max(ll.ts)                                                   " ;
        qs = qs + (old ? "ASC" : "DESC") + "\n";
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("userIDs", userIDs)
                .setFirstResult(page * count)
                .setMaxResults(count);
        return query.getResultList();
    }

    @Override
    public List<TweetDTO> getRecentTweetReplies(Long tweetID, boolean asc, int page, int count) {
        String qs = "\n" +
                "SELECT     t.id                            ID,                         \n" +
                "           t.user_id                       userID,                     \n" +
                "           t.message                       message,                    \n" +
                "           t.creationTimestamp             creationTS,                 \n" +
                "           rt.parent_tweet                 replyTo,                    \n" +
                "           COUNT(DISTINCT r.child_tweet)   replies,                    \n" +
                "           COUNT(DISTINCT l.user_id)       likes                       \n" +
                "   FROM tweets t                                                       \n" +
                "       LEFT JOIN replies r     ON t.id = r.parent_tweet                \n" +
                "       LEFT JOIN replies rt    ON t.id = rt.child_tweet                \n" +
                "       LEFT JOIN likes   l     ON t.id = l.tweet_id                    \n" +
                "   WHERE   rt.parent_tweet = :tweetID                                  \n" +
                "   GROUP BY t.id                                                       \n" +
                "   ORDER BY creationTS                                                 \n";
        qs = qs + (asc ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("tweetID", tweetID)
                .setFirstResult(page * count)
                .setMaxResults(count);
        return query.getResultList();
    }

    @Override
    public List<TweetDTO> getLikedTweetReplies(Long tweetID, boolean asc, int page, int count) {
        String qs = "\n" +
                "SELECT     t.id                            ID,                         \n" +
                "           t.user_id                       userID,                     \n" +
                "           t.message                       message,                    \n" +
                "           t.creationTimestamp             creationTS,                 \n" +
                "           rt.parent_tweet                 replyTo,                    \n" +
                "           COUNT(DISTINCT r.child_tweet)   replies,                    \n" +
                "           COUNT(DISTINCT l.user_id)       likes                       \n" +
                "   FROM tweets t                                                       \n" +
                "       LEFT JOIN replies r     ON t.id = r.parent_tweet                \n" +
                "       LEFT JOIN replies rt    ON t.id = rt.child_tweet                \n" +
                "       LEFT JOIN likes   l     ON t.id = l.tweet_id                    \n" +
                "   WHERE   rt.parent_tweet = :tweetID                                  \n" +
                "   GROUP BY t.id                                                       \n" +
                "   ORDER BY likes                                                      \n";
        qs = qs + (asc ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("tweetID", tweetID)
                .setFirstResult(page * count)
                .setMaxResults(count);
        return query.getResultList();
    }

    @Override
    public List<TweetDTO> getRepliedTweetReplies(Long tweetID, boolean asc, int page, int count) {
        String qs = "\n" +
                "SELECT     t.id                            ID,                         \n" +
                "           t.user_id                       userID,                     \n" +
                "           t.message                       message,                    \n" +
                "           t.creationTimestamp             creationTS,                 \n" +
                "           rt.parent_tweet                 replyTo,                    \n" +
                "           COUNT(DISTINCT r.child_tweet)   replies,                    \n" +
                "           COUNT(DISTINCT l.user_id)       likes                       \n" +
                "   FROM tweets t                                                       \n" +
                "       LEFT JOIN replies r     ON t.id = r.parent_tweet                \n" +
                "       LEFT JOIN replies rt    ON t.id = rt.child_tweet                \n" +
                "       LEFT JOIN likes   l     ON t.id = l.tweet_id                    \n" +
                "   WHERE   rt.parent_tweet = :tweetID                                  \n" +
                "   GROUP BY t.id                                                       \n" +
                "   ORDER BY replies                                                    \n";
        qs = qs + (asc ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("tweetID", tweetID)
                .setFirstResult(page * count)
                .setMaxResults(count);
        return query.getResultList();
    }

}

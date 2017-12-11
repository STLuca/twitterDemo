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
    public Tweet findTweetByID(Long id){
        return em.getReference(Tweet.class, id);
    }

    /*@Override
    public Tweet find(Long id) {
        return em.find(Tweet.class, id);
    }*/

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

    @Override
    public List<TweetDTO> getTweet(Long tweetID, Long myID) {
        String qs = "\n" +
                "SELECT *, t.id IN                                                          \n" +
                "   (SELECT l.tweet_id FROM likes l WHERE l.user_id = :myID) ILiked         \n" +
                "   FROM tweetSummary t                                                     \n" +
                "   WHERE   t.id = :tweetID                                                 \n";
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("tweetID", tweetID)
                .setParameter("myID", myID);
        return query.getResultList();

    }

    public void likeTweet(Long tweetID, Long myID, Date date){
        String qs = "INSERT IGNORE INTO likes VALUES" +
                    "(:userID, :tweetID, :date)";
        Query query = em.createNativeQuery(qs)
                .setParameter("userID", myID)
                .setParameter("tweetID", tweetID)
                .setParameter("date", date);
        query.executeUpdate();
    }

    public void unlikeTweet(Long tweetID, Long myID){
        String qs = "DELETE FROM likes " +
                    "WHERE user_id = :userID AND tweet_id = :tweetID";
        Query query = em.createNativeQuery(qs)
                .setParameter("userID", myID)
                .setParameter("tweetID", tweetID);
        query.executeUpdate();
    }

    @Override
    public List<TweetDTO> getRecentTweetsByUsers(List<Long> userIDs, Long myID, boolean asc, int page, int count) {
        String qs = "\n" +
                "SELECT *, t.id IN                                                          \n" +
                "   (SELECT l.tweet_id FROM likes l WHERE l.user_id = :myID) ILiked         \n" +
                " FROM tweetSummary t                                                       \n"+
                "   WHERE t.userID IN (:userIDs)                                            \n"+
                "   ORDER BY t.creationTS ";
        qs = qs + (asc ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("userIDs", userIDs)
                .setParameter("myID", myID);
        return getPagedQueryResult(query, page, count);
    }

    @Override
    public List<TweetDTO> getLikedTweetsByUsers(List<Long> userIDs, Long myID, boolean asc, Date withinDate, int page, int count) {
        String qs = "\n" +
                "SELECT *, t.id IN                                                          \n" +
                "   (SELECT l.tweet_id FROM likes l WHERE l.user_id = :myID) ILiked         \n" +
                "FROM tweetSummary t                                                        \n"+
                "   WHERE   t.userID IN (:userIDs)   AND                                    \n"+
                "           t.creationTS > :date                                            \n"+
                "   ORDER BY t.likes ";
        qs = qs + (asc ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("userIDs", userIDs)
                .setParameter("date", withinDate)
                .setParameter("myID", myID);
        return getPagedQueryResult(query, page, count);
    }

    @Override
    public List<TweetDTO> getRepliedTweetsByUsers(List<Long> userIDs, Long myID, boolean asc, Date withinDate, int page, int count) {
        String qs = "\n" +
                "SELECT *, t.id IN                                                          \n" +
                "   (SELECT l.tweet_id FROM likes l WHERE l.user_id = :myID) ILiked         \n" +
                " FROM tweetSummary t                                                       \n"+
                "   WHERE   t.userID IN (:userIDs)   AND                                    \n"+
                "           t.creationTS > :date                                            \n"+
                "   ORDER BY t.replies ";
        qs = qs + (asc ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("userIDs", userIDs)
                .setParameter("date", withinDate)
                .setParameter("myID", myID);
        return getPagedQueryResult(query, page, count);
    }

    @Override
    public List<TweetDTO> getUsersLikedTweets(List<Long> userIDs, Long myID, boolean old, int page, int count) {
        String qs = "\n" +
                "SELECT DISTINCT *, t.id IN                                                 \n" +
                "   (SELECT l.tweet_id FROM likes l WHERE l.user_id = :myID) ILiked         \n" +
                " FROM tweetSummary t                                                       \n" +
                "   JOIN likes l ON t.id = l.tweet_id AND                                   \n" +
                "                   l.user_id in (:userIDs)                                 \n" +
                "   GROUP BY t.id                                                           \n" +
                "   ORDER BY max(l.ts) ";
        qs = qs + (old ? "ASC" : "DESC") + "\n";
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("userIDs", userIDs)
                .setParameter("myID", myID);
        return getPagedQueryResult(query, page, count);
    }

    @Override
    public List<TweetDTO> getRecentTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count) {
        String qs = "\n" +
                "SELECT *, t.id IN                                                          \n" +
                "   (SELECT l.tweet_id FROM likes l WHERE l.user_id = :myID) ILiked         \n" +
                " FROM tweetSummary t                                                       \n" +
                "   WHERE t.replyTo = :tweetID                                              \n" +
                "   ORDER BY t.creationTS                                                   \n";
        qs = qs + (asc ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("tweetID", tweetID)
                .setParameter("myID", myID);
        return getPagedQueryResult(query, page, count);
    }

    @Override
    public List<TweetDTO> getLikedTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count) {
        String qs = "\n" +
                "SELECT *, t.id IN " +
                "   (SELECT l.tweet_id FROM likes l WHERE l.user_id = :myID) ILiked         \n" +
                "   FROM tweetSummary t                                                     \n" +
                "   WHERE t.replyTo = :tweetID                                              \n" +
                "   ORDER BY t.likes                                                        \n";
        qs = qs + (asc ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("tweetID", tweetID)
                .setParameter("myID", myID);
        return getPagedQueryResult(query, page, count);
    }

    @Override
    public List<TweetDTO> getRepliedTweetReplies(Long tweetID, Long myID, boolean asc, int page, int count) {
        String qs = "\n" +
                "SELECT *, t.id IN                                                          \n" +
                "   (SELECT l.tweet_id FROM likes l WHERE l.user_id = :myID) ILiked         \n" +
                "   FROM tweetSummary t                                                     \n" +
                "   WHERE t.replyTo = :tweetID                                              \n" +
                "   ORDER BY t.replies                                                      \n";
        qs = qs + (asc ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "TweetDTOMapping")
                .setParameter("tweetID", tweetID)
                .setParameter("myID", myID);
        return getPagedQueryResult(query, page, count);
    }

    private List<TweetDTO> getPagedQueryResult(Query query, int page, int count){
        query.setFirstResult(page * count)
                .setMaxResults(count);
        return query.getResultList();
    }


}

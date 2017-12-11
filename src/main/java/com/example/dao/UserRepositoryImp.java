package com.example.dao;


import com.example.DataTransfer.UserDTO;
import com.example.Entities.User;
import com.example.Exception.exceptions.DuplicateEmailException;
import com.example.Exception.exceptions.DuplicateUsernameException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Repository
public class UserRepositoryImp implements JpaUserRepository{

    @PersistenceContext
    private EntityManager em;


    public User findUserByID(Long id){
        return em.getReference(User.class, id);
    }

    //IllegalArgumentException - if the first argument does not denote an entity type or the second argument is is not a valid type for that entity’s primary key or is null
    //public User findById(Long id){

    //    return em.find(User.class, id);

   // }

    /*
        NoResultException - if there is no result
        NonUniqueResultException - if more than one result
     */
    public User findByUsername(String username){

        //TypedQuery<User> query =  em.createQuery("SELECT u from USERS where u.username = :username", User.class)
                //.setParameter("username", username);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> u = criteria.from(User.class);
        criteria.select(u).where(cb.equal(u.get("username"), username));

        TypedQuery<User> query = em.createQuery(criteria);

        return query.getSingleResult();

    }
    /*persist
    EntityExistsException - if the entity already exists. (If the entity already exists, the EntityExistsException may be thrown when the persist operation is invoked, or the
        EntityExistsException or another PersistenceException may be thrown at flush or commit time.)
    IllegalArgumentException - if the instance is not an entity
    PersistenceException - if the flush fails
     */
    public void save(User user){

        if (!checkUniqueUsername(user.getUsername())){
            throw new DuplicateUsernameException(user.getUsername());
        }

        if (!checkUniqueEmail(user.getEmail())){
            throw new DuplicateEmailException(user.getEmail());
        }

        em.persist(user);
    }

    private boolean checkUniqueUsername(String username){
        TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username);
        return q.getResultList().size() == 0;
    }

    private boolean checkUniqueEmail(String email){
        TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email);
        return q.getResultList().size() == 0;
    }

    @Override
    //IllegalArgumentException - if instance is not an entity or is a removed entity
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public void removeById(Long id) {
        User user = em.getReference(User.class, id);
        em.remove(user);
    }

    //IllegalArgumentException - if the instance is not an entity or is a detached entity
    public void removeByUser(User user){
        em.merge(user);
        em.remove(user);
    }


    @Override
    public List<UserDTO> getUserByUsername(String username, Long myID) {
        String qs = "" +
                "SELECT u.*, u.id IN                                                            \n" +
                "   (SELECT f.followee_id FROM follows f WHERE f.follower_id = :myID) IFollow   \n" +
                " FROM usersSummary u   \n" +
                "WHERE u.username = :username";
        Query query = em.createNativeQuery(qs, "UserDTOMapping")
                .setParameter("username", username)
                .setParameter("myID", myID);
        return query.getResultList();

    }

    @Override
    public List<UserDTO> getUsersByIDs(Set<Long> ids, Long myID) {
        String qs = "" +
                "SELECT u.*, u.id IN                                                            \n" +
                "   (SELECT f.followee_id FROM follows f WHERE f.follower_id = :myID) IFollow   \n" +
                "FROM usersSummary u                                                            \n" +
                "WHERE u.id IN (:ids)                             ";
        Query query = em.createNativeQuery(qs, "UserDTOMapping")
                .setParameter("ids", ids)
                .setParameter("myID", myID);
        return  query.getResultList();
    }

    public void followUser(Long userID, Long myID){
        String qs = "INSERT IGNORE INTO follows " +
                    "VALUES (:userID, :myID, :date)";
        Query query = em.createNativeQuery(qs)
                .setParameter("userID", userID)
                .setParameter("myID", myID)
                .setParameter("date", new Date());
        query.executeUpdate();
    }

    public void unfollowUser(Long userID, Long myID){
        String qs = "DELETE FROM follows " +
                    "WHERE followee_id = :userID AND follower_id = :myID";
        Query query = em.createNativeQuery(qs)
                .setParameter("userID", userID)
                .setParameter("myID", myID);
        query.executeUpdate();
    }

    public List<UserDTO> getFollowingByUsername(String username, Long myID, boolean old, int page, int count){
        User user = findByUsername(username);
        return getFollowingByID(user.getId(), myID, old, page, count);
    }

    @Override
    public List<UserDTO> getFollowingByID(Long userID, Long myID, boolean old, int page, int count) {
        String qs = "" +
                "SELECT u.*, u.id IN                                                            \n" +
                "   (SELECT f.followee_id FROM follows f WHERE f.follower_id = :myID) IFollow   \n" +
                " FROM usersSummary u                           \n" +
                "   JOIN follows f ON u.id = f.followee_id      \n" +
                "   WHERE f.follower_id = :userID                   \n" +
                "   ORDER BY f.ts ";
        qs = qs + (old ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "UserDTOMapping")
                .setParameter("userID", userID)
                .setParameter("myID", myID);
        return getPagedQueryResults(query, page, count);
    }

    public List<UserDTO> getFollowersByUsername(String username, Long myID, boolean old, int page, int count){
        User user = findByUsername(username);
        return getFollowersByID(user.getId(), myID, old, page, count);
    }

    @Override
    public List<UserDTO> getFollowersByID(Long userID, Long myID, boolean old, int page, int count) {
        String qs = "" +
                "SELECT u.*, u.id IN                                                            \n" +
                "   (SELECT f.followee_id FROM follows f WHERE f.follower_id = :myID) IFollow   \n" +
                " FROM usersSummary u                           \n" +
                "   JOIN follows f ON u.id = f.follower_id      \n" +
                "   WHERE f.followee_id = :userID               \n" +
                "   ORDER BY f.ts ";
        qs = qs + (old ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "UserDTOMapping")
                .setParameter("userID", userID)
                .setParameter("myID", myID);
        return getPagedQueryResults(query, page, count);

    }

    private List<UserDTO> getPagedQueryResults(Query query, int page, int count){
        query.setFirstResult(page * count)
                .setMaxResults(count);
        return query.getResultList();
    }
}

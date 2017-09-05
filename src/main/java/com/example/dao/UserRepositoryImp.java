package com.example.dao;


import com.example.DataTransfer.UserDTO;
import com.example.Entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;


@Repository
public class UserRepositoryImp implements JpaUserRepository{

    @PersistenceContext
    private EntityManager em;


    public User getReference(Long id){
        return em.getReference(User.class, id);
    }

    //IllegalArgumentException - if the first argument does not denote an entity type or the second argument is is not a valid type for that entity’s primary key or is null
    public User findById(Long id){

        return em.find(User.class, id);

    }

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
        em.persist(user);
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
    public List<UserDTO> getUserByUsername(String username) {
        Query query = em.createNativeQuery("SELECT \tu.id,\n" +
                "\t\tu.username,\n" +
                "\t\tCOUNT(DISTINCT t.id) \t\t   \ttweets,\n" +
                "\t\tCOUNT(DISTINCT f.follower_id) \tfollowers,\n" +
                "\t\tCOUNT(DISTINCT f2.followee_id) \tfollowing\n" +
                "\tFROM users u\n" +
                "\t\tLEFT JOIN tweets \tt \tON u.id = t.user_id\n" +
                "\t\tLEFT JOIN follows f \tON u.id = f.followee_id\n" +
                "\t\tLEFT JOIN follows f2 \tON u.id = f2.follower_id\n" +
                "\tWHERE u.username = :username\n" +
                "\tGROUP BY u.id;", "UserDTOMapping").setParameter("username", username);
        return query.getResultList();

    }

    @Override
    public List<UserDTO> getUsersByIDs(Set<Long> ids) {
        String qs = "" +
                "SELECT u.id                            id,         \n" +
                "       u.username                      username,   \n" +
                "       COUNT(DISTINCT t.id)            tweets,     \n" +
                "       COUNT(DISTINCT f.follower_id)   followers,  \n" +
                "       COUNT(DISTINCT f2.followee_id)  following   \n" +
                "FROM users u                                       \n" +
                "   LEFT JOIN tweets  t  ON u.id = t.user_id        \n" +
                "   LEFT JOIN follows f  ON u.id = f.followee_id    \n" +
                "   LEFT JOIN follows f2 ON u.id = f2.follower_id   \n" +
                "WHERE u.id IN (:ids)                               \n" +
                "GROUP BY u.id;                                     \n";
        Query query = em.createNativeQuery(qs, "UserDTOMapping")
                .setParameter("ids", ids);
        return  query.getResultList();
    }

    public List<UserDTO> getFollowingByUsername(String username, boolean old, int page, int count){
        User user = findByUsername(username);
        return getFollowingByID(user.getId(), old, page, count);
    }

    @Override
    public List<UserDTO> getFollowingByID(Long id, boolean old, int page, int count) {
        String qs = "" +
                "SELECT u.id                            id,         \n" +
                "       u.username                      username,   \n" +
                "       COUNT(DISTINCT t.id)            tweets,     \n" +
                "       COUNT(DISTINCT f.follower_id)   followers,  \n" +
                "       COUNT(DISTINCT f2.followee_id)  following   \n" +
                "FROM users u                                       \n" +
                "   LEFT JOIN tweets  t  ON u.id = t.user_id        \n" +
                "   LEFT JOIN follows f  ON u.id = f.followee_id    \n" +
                "   LEFT JOIN follows f2 ON u.id = f2.follower_id   \n" +
                "WHERE u.id in                                      \n" +
                "   (select f3.followee_id from follows f3          \n" +
                "           where f3.follower_id = :id)             \n" +
                "GROUP BY u.id                                      \n" +
                "HAVING followers > 0                               \n" +
                "ORDER BY f.ts ";
        qs = qs + (old ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "UserDTOMapping")
                .setParameter("id", id)
                .setFirstResult(page * count)
                .setMaxResults(count);
        return  query.getResultList();
    }

    public List<UserDTO> getFollowersByUsername(String username, boolean old, int page, int count){
        User user = findByUsername(username);
        return getFollowersByID(user.getId(), old, page, count);
    }

    @Override
    public List<UserDTO> getFollowersByID(Long id, boolean old, int page, int count) {
        String qs = "" +
                "SELECT u.id                            id,         \n" +
                "       u.username                      username,   \n" +
                "       COUNT(DISTINCT t.id)            tweets,     \n" +
                "       COUNT(DISTINCT f.follower_id)   followers,  \n" +
                "       COUNT(DISTINCT f2.followee_id)  following   \n" +
                "FROM users u                                       \n" +
                "   LEFT JOIN tweets  t  ON u.id = t.user_id        \n" +
                "   LEFT JOIN follows f  ON u.id = f.followee_id    \n" +
                "   LEFT JOIN follows f2 ON u.id = f2.follower_id   \n" +
                "WHERE u.id in                                      \n" +
                "   (select f3.follower_id from follows f3          \n" +
                "           where f3.followee_id = :id)             \n" +
                "GROUP BY u.id                                      \n" +
                "HAVING following > 0                               \n" +
                "ORDER BY f2.ts ";
        qs = qs + (old ? "ASC" : "DESC");
        Query query = em.createNativeQuery(qs, "UserDTOMapping")
                .setParameter("id", id)
                .setFirstResult(page * count)
                .setMaxResults(count);
        return  query.getResultList();

    }

}

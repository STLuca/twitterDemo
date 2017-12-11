package com.example.dao;

import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface JpaUserRepository {

    User findUserByID(Long id);
    //User findById(Long id);
    User findByUsername(String username);
    void save(User user);
    void update(User user);
    void removeById(Long id);
    void removeByUser(User user);

    List<UserDTO> getUserByUsername(String username, Long myID);
    List<UserDTO> getUsersByIDs(Set<Long> id, Long myID);

    void followUser(Long userID, Long myID);
    void unfollowUser(Long userID, Long myID);

    List<UserDTO> getFollowersByUsername(String username, Long myID, boolean old, int page, int count);
    List<UserDTO> getFollowingByUsername(String username, Long myID, boolean old, int page, int count);
    List<UserDTO> getFollowersByID(Long id, Long myID, boolean old, int page, int count);
    List<UserDTO> getFollowingByID(Long id, Long myID, boolean old, int page, int count);



}

package com.example.dao;

import com.example.DataTransfer.UserDTO;
import com.example.Entities.Tweet;
import com.example.Entities.User;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface JpaUserRepository {

    User getReference(Long id);
    User findById(Long id);
    User findByUsername(String username);
    void save(User user);
    void update(User user);
    void removeById(Long id);
    void removeByUser(User user);

    List<UserDTO> getUserByUsername(String username);
    List<UserDTO> getUsersByIDs(Set<Long> id);

    List<UserDTO> getFollowersByUsername(String username, boolean old, int page, int count);
    List<UserDTO> getFollowingByUsername(String username, boolean old, int page, int count);
    List<UserDTO> getFollowersByID(Long id, boolean old, int page, int count);
    List<UserDTO> getFollowingByID(Long id, boolean old, int page, int count);



}

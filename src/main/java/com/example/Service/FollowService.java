package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.UserDTO;

import java.util.List;

public interface FollowService {

    void followUser(Long userID, String followeeUsername);
    void unfollowUser(Long userID, String followeeUsername);
    CombinedDTO getFollowing(String username, Long myID, boolean old, int page, int count);
    CombinedDTO getFollowers(String username, Long myID, boolean old, int page, int count);

}

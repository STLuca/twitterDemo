package com.example.Service;

import com.example.DataTransfer.CombinedDTO;
import com.example.DataTransfer.UserDTO;
import com.example.Entities.User;
import com.example.dao.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FollowServiceImp implements FollowService{


    private JpaUserRepository userRepository;

    @Autowired
    public FollowServiceImp(JpaUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void followUser(Long userID, String followeeUsername){

        User user = userRepository.findUserByID(userID);
        User followee = userRepository.findByUsername(followeeUsername);
        user.followUser(followee);

    }

    @Override
    public void unfollowUser(Long userID, String followeeUsername){
        User user = userRepository.findUserByID(userID);
        User followee = userRepository.findByUsername(followeeUsername);
        user.unfollowUser(followee);

    }

    @Override
    public CombinedDTO getFollowing(String username, Long myID, boolean old, int page, int count) {
        return CombinedDTO.createFromUsers(userRepository.getFollowingByUsername(username, myID, old, page, count));
    }

    @Override
    public CombinedDTO getFollowers(String username, Long myID, boolean old, int page, int count) {
        return CombinedDTO.createFromUsers(userRepository.getFollowersByUsername(username, myID, old, page, count));
    }

}

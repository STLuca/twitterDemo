package com.example.Service;

import com.example.DataTransfer.CombinedDTO;

import java.util.Date;


public interface FeedService {

    CombinedDTO getRecentFeed(Long id, int page, int count);
    CombinedDTO getMostLikedFeed(Long id,  int days, int page, int count);
    CombinedDTO getMostRepliedFeed(Long id,  int days, int page, int count);
    CombinedDTO getLikesFeed(Long id, int page, int count);
}

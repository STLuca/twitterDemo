package com.example.Service;

import com.example.DataTransfer.CombinedDTO;

import java.util.Date;


public interface FeedService {

    CombinedDTO getRecentFeed(Long id, boolean asc, int page, int count);
    CombinedDTO getLikedFeed(Long id, boolean asc, int days, int page, int count);
    CombinedDTO getRepliedFeed(Long id, boolean asc, int days, int page, int count);
    CombinedDTO getLikesFeed(Long id, boolean asc, int page, int count);
}

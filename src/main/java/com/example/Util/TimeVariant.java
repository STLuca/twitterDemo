package com.example.Util;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class TimeVariant implements ITimeVariant{

    private static final Long DAY_IN_MS = new Long(1000 * 60 * 60 * 24);

    @Override
    public Date getDateFromXDaysAgo(int x) {
        return new Date(System.currentTimeMillis() - (x * DAY_IN_MS));
    }

}

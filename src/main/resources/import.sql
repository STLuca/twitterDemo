CREATE VIEW tweetSummary as
SELECT
		t.id ID,
		t.user_id                       userID,
		t.message                       message,
		t.creationTimestamp             creationTS,
		rt.parent_tweet                 replyTo,
		COUNT(DISTINCT r.child_tweet)   replies,
		COUNT(DISTINCT l.user_id)       likes
	FROM tweets t
		LEFT JOIN replies r     ON t.id = r.parent_tweet
		LEFT JOIN replies rt    ON t.id = rt.child_tweet
		LEFT JOIN likes   l     ON t.id = l.tweet_id
	GROUP BY t.id;

CREATE VIEW usersSummary AS
SELECT 	u.id,
		u.username,
		COUNT(DISTINCT t.id)    tweets,
		COUNT(DISTINCT f.follower_id) followers,
		COUNT(DISTINCT f2.followee_id) following
	FROM 		  users u
		LEFT JOIN tweets t ON u.id = t.user_id
		LEFT JOIN follows f ON u.id = f.followee_id
		LEFT JOIN follows f2 ON u.id = f2.follower_id
	group by u.id;

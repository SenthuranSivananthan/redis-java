package com.senthuran.redis.pubsub.sync.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lambdaworks.redis.pubsub.RedisPubSubListener;

/**
 * Example listener that will rebuild the cache based on the data passed to the
 * channel.
 * 
 * @author Senthuran Sivananthan
 *
 */
public final class RebuildCacheListener implements RedisPubSubListener<String, String> {
	private static final Logger logger = LogManager.getLogger(RebuildCacheListener.class);

	public void message(String channel, String message) {
		logger.info("Channel: {}, Message: {}", channel, message);
	}

	public void message(String pattern, String channel, String message) {
		logger.info("Channel: {}, Pattern: {}, Message: {}", channel, pattern, message);
	}

	public void psubscribed(String channel, long count) {
		// TODO Auto-generated method stub
	}

	public void punsubscribed(String pattern, long count) {
		// TODO Auto-generated method stub

	}

	public void subscribed(String channel, long count) {
		// TODO Auto-generated method stub

	}

	public void unsubscribed(String channel, long count) {
		// TODO Auto-generated method stub
	}
}

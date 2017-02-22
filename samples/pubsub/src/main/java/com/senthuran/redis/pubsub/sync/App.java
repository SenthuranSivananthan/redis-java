package com.senthuran.redis.pubsub.sync;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.pubsub.api.sync.RedisPubSubCommands;
import com.senthuran.redis.pubsub.sync.listeners.FlushCacheListener;
import com.senthuran.redis.pubsub.sync.listeners.RebuildCacheListener;

public final class App {
	private final static String CACHE_LISTENER_FLUSH_KEY = "/HelloWorldApp/CacheOps/Flush";
	private final static String CACHE_LISTENER_REBUILD_KEY = "/HelloWorldApp/CacheOps/Rebuild";

	public static void main(String[] args) throws Exception {
		String redisPassword = System.getenv("REDIS_PASSWORD");
		String redisHost = System.getenv("REDIS_HOSTNAME");
		int redisPort = Integer.parseInt(System.getenv("REDIS_PORT"));

		RedisURI redisUri = RedisURI.Builder.redis(redisHost)
				.withSsl(true)
				.withPassword(redisPassword)
				.withPort(redisPort)
                .withDatabase(0)
                .build();
		
		// Create Redis Client
		RedisClient redisClient = RedisClient.create(redisUri);

		// Create Redis Connection
		StatefulRedisConnection<String, String> redis = redisClient.connect();

		// Register Listener to handle events
		registerFlushCacheListener(redisClient);
		registerRebuildCacheListener(redisClient);

		// Publish multiple messages.  Each should be handled by it's own listener
		redis.sync().publish(CACHE_LISTENER_FLUSH_KEY, "flush_accounts_key");
		redis.sync().publish(CACHE_LISTENER_REBUILD_KEY, "rebuild_statistics");

		redisClient.shutdown();
	}

	private static void registerFlushCacheListener(RedisClient client) {
		StatefulRedisPubSubConnection<String, String> connection = client.connectPubSub();
		connection.addListener(new FlushCacheListener());

		RedisPubSubCommands<String, String> sync = connection.sync();
		sync.subscribe(CACHE_LISTENER_FLUSH_KEY);
	}

	private static void registerRebuildCacheListener(RedisClient client) {
		StatefulRedisPubSubConnection<String, String> connection = client.connectPubSub();
		connection.addListener(new RebuildCacheListener());

		RedisPubSubCommands<String, String> sync = connection.sync();
		sync.subscribe(CACHE_LISTENER_REBUILD_KEY);
	}
}

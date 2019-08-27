package com.accp.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accp.jedis.JedisClient;
import com.accp.jedis.impl.JedisClientCluster;
import com.accp.jedis.impl.JedisClientPool;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * Redis配置 1、项目中是通过jedis操作redis,
 * 2、项目中可以通过jedisClientPool或jedisClientCluster实现JedisClient
 * 
 * @author Luyuan
 * @date 2019年3月22日
 *
 */
@Log4j2
@EnableConfigurationProperties(RedisProperties.class)
@Configuration
public class RedisConfig {

	@Autowired
	private RedisProperties redisProperties;
	// 是否开启集群模式
	private boolean enableCluster = false;

	/**
	 * jedisClientPool()和jedisClientCluster()二选一
	 * 
	 * @return
	 */
	@Bean
	public JedisClient jedisClient() {
		JedisPoolConfig poolConfig = getJedisPoolConfig();

		int timeout = null == redisProperties.getTimeout() ? Protocol.DEFAULT_TIMEOUT
				: Long.valueOf(redisProperties.getTimeout().getSeconds()).intValue();

		if (enableCluster) {

			Cluster cluster = redisProperties.getCluster();

			List<String> list = cluster.getNodes();

			Set<HostAndPort> nodes = new HashSet<>();

			for (String node : list) {

				String[] values = node.split(":");

				String host = values[0].trim();

				int port = Integer.parseInt(values[1].trim());

				nodes.add(new HostAndPort(host, port));
			}

			return new JedisClientCluster(new JedisCluster(nodes, poolConfig));

		} else {

			JedisPool jedisPool = new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort(),
					timeout, redisProperties.getPassword(),redisProperties.getDatabase());

			return new JedisClientPool(jedisPool);
		}
	}

	private JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig poolConfig = null;
		try {
			poolConfig = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			poolConfig.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			poolConfig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
			// 控制一个pool最少有多少个状态为idle(空闲的)的jedis实例。
			poolConfig.setMinIdle(redisProperties.getJedis().getPool().getMinIdle());
			long maxWait = redisProperties.getJedis().getPool().getMaxWait().getSeconds();
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			poolConfig.setMaxWaitMillis(maxWait);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			poolConfig.setTestOnBorrow(true);
			// 在return给pool时，是否提前进行validate操作
			poolConfig.setTestOnReturn(true);
			// 如果为true，表示有一个idle object evitor线程对idle
			// object进行扫描，如果validate失败，此object会被从pool中drop掉；
			// 这一项只有在 timeBetweenEvictionRunsMillis大于0时才有意义
			poolConfig.setTestWhileIdle(true);
			// 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；
			// 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
			poolConfig.setMinEvictableIdleTimeMillis(60000L);
			// 表示idle object evitor两次扫描之间要sleep的毫秒数
			poolConfig.setTimeBetweenEvictionRunsMillis(3000L);
			// 表示idle object evitor每次扫描的最多的对象数；
			poolConfig.setNumTestsPerEvictionRun(-1);

		} catch (Exception e) {
			log.error("----[redisConfig]创建jedisPool失败，异常信息:{}", e.getMessage());
			e.printStackTrace();
		}
		return poolConfig;
	}

}

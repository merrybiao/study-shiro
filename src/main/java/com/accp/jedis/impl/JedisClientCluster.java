package com.accp.jedis.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.accp.jedis.JedisClient;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.JedisCluster;

/**
 * jedis操作redis集群模式
 * 
 * @author Luyuan
 */
public class JedisClientCluster implements JedisClient {

	private JedisCluster jedisCluster;

	public JedisClientCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public Boolean exists(String key) {
		return jedisCluster.exists(key);
	}

	@Override
	public Long expire(String key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public Long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return jedisCluster.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedisCluster.hget(key, field);
	}

	@Override
	public Long hdel(String key, String... field) {
		return jedisCluster.hdel(key, field);
	}

	@Override
	public Boolean hexists(String key, String field) {
		return jedisCluster.hexists(key, field);
	}

	@Override
	public List<String> hvals(String key) {
		return jedisCluster.hvals(key);
	}

	@Override
	public Long del(String... keys) {
		return jedisCluster.del(keys);
	}

	@Override
	public Long append(String key, String value) {
		return jedisCluster.append(key, value);
	}

	@Override
	public Long setnx(String key, String value) {
		return jedisCluster.setnx(key, value);
	}

	@Override
	public String setex(String key, String value, int seconds) {
		return jedisCluster.setex(key, seconds, value);
	}

	@Override
	public Long setrange(String key, String value, int offset) {
		return jedisCluster.setrange(key, offset, value);
	}

	@Override
	public List<String> mget(String... keys) {
		return jedisCluster.mget(keys);
	}

	@Override
	public String mset(String... keysvalues) {
		return jedisCluster.mset(keysvalues);
	}

	@Override
	public Long msetnx(String... keysvalues) {
		return jedisCluster.msetnx(keysvalues);
	}

	@Override
	public String getset(String key, String value) {
		return jedisCluster.getSet(key, value);
	}

	@Override
	public String getrange(String key, int startOffset, int endOffset) {
		return jedisCluster.getrange(key, startOffset, endOffset);
	}

	@Override
	public Long incrBy(String key, Long integer) {
		return jedisCluster.incrBy(key, integer);
	}

	@Override
	public Long decr(String key) {
		return jedisCluster.decr(key);
	}

	@Override
	public Long decrBy(String key, Long integer) {
		return jedisCluster.decrBy(key, integer);
	}

	@Override
	public Long strlen(String key) {
		return jedisCluster.strlen(key);
	}

	@Override
	public Long hsetnx(String key, String field, String value) {
		return jedisCluster.hsetnx(key, field, value);
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		return jedisCluster.hmset(key, hash);
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		return jedisCluster.hmget(key, fields);
	}

	@Override
	public Long hincrby(String key, String field, Long value) {
		return jedisCluster.hincrBy(key, field, value);
	}

	@Override
	public Long hlen(String key) {
		return jedisCluster.hlen(key);
	}

	@Override
	public Set<String> hkeys(String key) {
		return jedisCluster.hkeys(key);
	}

	@Override
	public Map<String, String> hgetall(String key) {
		return jedisCluster.hgetAll(key);
	}

	@Override
	public Long lpush(String key, String... strs) {
		return jedisCluster.lpush(key, strs);
	}

	@Override
	public Long rpush(String key, String... strs) {
		return jedisCluster.rpush(key, strs);
	}

	@Override
	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		return jedisCluster.linsert(key, where, pivot, value);
	}

	@Override
	public String lset(String key, Long index, String value) {
		return jedisCluster.lset(key, index, value);
	}

	@Override
	public Long lrem(String key, long count, String value) {
		return jedisCluster.lrem(key, count, value);
	}

	@Override
	public String ltrim(String key, long start, long end) {
		return jedisCluster.ltrim(key, start, end);
	}

	@Override
	public String lpop(String key) {
		return jedisCluster.lpop(key);
	}

	@Override
	public String rpop(String key) {
		return jedisCluster.rpop(key);
	}

	@Override
	public String rpoplpush(String srckey, String dstkey) {
		return jedisCluster.rpoplpush(srckey, dstkey);
	}

	@Override
	public String lindex(String key, long index) {
		return jedisCluster.lindex(key, index);
	}

	@Override
	public Long llen(String key) {
		return jedisCluster.llen(key);
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return jedisCluster.lrange(key, start, end);
	}

	@Override
	public Long sadd(String key, String... members) {
		return jedisCluster.sadd(key, members);
	}

	@Override
	public Long srem(String key, String... members) {
		return jedisCluster.srem(key, members);
	}

	@Override
	public String spop(String key) {
		return jedisCluster.spop(key);
	}

	@Override
	public Set<String> sdiff(String... keys) {
		return jedisCluster.sdiff(keys);
	}

	@Override
	public Long sdiffstore(String dstkey, String... keys) {
		return jedisCluster.sdiffstore(dstkey, keys);
	}

	@Override
	public Set<String> sinter(String... keys) {
		return jedisCluster.sinter(keys);
	}

	@Override
	public Long sinterstore(String dstkey, String... keys) {
		return jedisCluster.sinterstore(dstkey, keys);
	}

	@Override
	public Set<String> sunion(String... keys) {
		return jedisCluster.sunion(keys);
	}

	@Override
	public Long sunionstore(String dstkey, String... keys) {
		return jedisCluster.sunionstore(dstkey, keys);
	}

	@Override
	public Long smove(String srckey, String dstkey, String member) {
		return jedisCluster.smove(srckey, dstkey, member);
	}

	@Override
	public Long scard(String key) {
		return jedisCluster.scard(key);
	}

	@Override
	public Boolean sismember(String key, String member) {
		return jedisCluster.sismember(key, member);
	}

	@Override
	public String srandmember(String key) {
		return jedisCluster.srandmember(key);
	}

	@Override
	public Set<String> smembers(String key) {
		return jedisCluster.smembers(key);
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		return jedisCluster.zadd(key, scoreMembers);
	}

	@Override
	public Long zadd(String key, double score, String member) {
		return jedisCluster.zadd(key, score, member);
	}

	@Override
	public Long zrem(String key, String... members) {
		return jedisCluster.zrem(key, members);
	}

	@Override
	public Double zincrby(String key, double score, String member) {
		return jedisCluster.zincrby(key, score, member);
	}

	@Override
	public Long zrank(String key, String member) {
		return jedisCluster.zrank(key, member);
	}

	@Override
	public Long zrevrank(String key, String member) {
		return jedisCluster.zrevrank(key, member);
	}

	@Override
	public Set<String> zrevrange(String key, long start, long end) {
		return jedisCluster.zrevrange(key, start, end);
	}

	@Override
	public Set<String> zrangebyscore(String key, String max, String min) {
		return jedisCluster.zrangeByScore(key, min, max);
	}

	@Override
	public Set<String> zrangeByScore(String key, double max, double min) {
		return jedisCluster.zrangeByScore(key, min, max);
	}

	@Override
	public Long zcount(String key, String min, String max) {
		return jedisCluster.zcount(key, min, max);
	}

	@Override
	public Long zcard(String key) {
		return jedisCluster.zcard(key);
	}

	@Override
	public Double zscore(String key, String member) {
		return jedisCluster.zscore(key, member);
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		return jedisCluster.zremrangeByRank(key, start, end);
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		return jedisCluster.zremrangeByScore(key, start, end);
	}

	@Override
	public Set<String> keys(String pattern) {
		return jedisCluster.hkeys(pattern);
	}

	@Override
	public String type(String key) {
		return jedisCluster.type(key);
	}

	@Override
	public Long setNxAndEx(String key, String value, int seconds) {
		return jedisCluster.setnx(key, value);
	}

	@Override
	public Long hincr(String key, String field) {
		return jedisCluster.hincrBy(key, field, 1);
	}

}

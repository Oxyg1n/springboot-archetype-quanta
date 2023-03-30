package com.linine.archetype.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author gdufs
 * modify by linine
 * @since 2021/11/25
 */
@Slf4j
@Component // 实现bean的注入
@SuppressWarnings("ConstantConditions")
public class RedisUtils {


    @Autowired
    @Qualifier("redisTemplate") //自定义缓存配置类
    private RedisTemplate<String, Object> redisTemplate;

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }


    // string数据类型

    /**
     * 通过key获得值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 通过key写值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 通过key写值，设置缓存时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间，单位:秒，负数为不设置时效
     * @return
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) redisTemplate.delete(key[0]);
            else redisTemplate.delete(Arrays.asList(key));
        }
    }

    /**
     * 获得键剩余时间
     *
     * @param key 键
     * @return 时间
     */
    public Long getExpire(String key) {
        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }

    /**
     * 设置键过期时间
     * @param key 键
     * @param time 时间
     * @return
     */
    public boolean setExpire(String key, long time) {
        try {
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获得类型为int的键值
     * 不存在则返回0
     *
     * @param key 键
     * @return
     */
    public int getIntOrZero(String key) {
        return redisTemplate.opsForValue().get(key) == null ? 0 : (int) redisTemplate.opsForValue().get(key);
    }

    /**
     * 键值增加
     *
     * @param key   键
     * @param delta 增值
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("Incr factor must greater than zero.");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 键值自增
     *
     * @param key 键
     * @return
     */
    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 键值减少
     *
     * @param key   键
     * @param delta 降值
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("Decr factor must greater than zero.");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    // HASHMAP

    /**
     * hash获得值
     *
     * @param key  键
     * @param item hash键
     * @return
     */
    public Object hashGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * hash批量获得值
     *
     * @param key 键
     * @return
     */
    public Map<Object, Object> hashMget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * hash批量添加值
     *
     * @param key 键
     * @param map hashKey和hashValue
     * @return
     */
    public boolean hashMset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * hash批量添加值，同时设置过期时间
     *
     * @param key  键
     * @param map  hashKey和hashValue
     * @param time 过期时间
     * @return
     */
    public boolean hashMset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                setExpire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * hash单条插入
     *
     * @param key   键
     * @param item  hash键
     * @param value 值
     * @return
     */
    public boolean hashSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * hash单条插入，同时设置过期时间
     *
     * @param key   键
     * @param item  hash键
     * @param value 值
     * @param time  过期时间
     * @return
     */
    public boolean hashSet(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                setExpire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * hash删除hashKey
     *
     * @param key  键
     * @param item hashKey
     */
    public void hashDel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * hash是否存在该hashKey
     *
     * @param key  键
     * @param item hashKey
     * @return
     */
    public boolean hashHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash的hash键值增加或减少
     *
     * @param key   键
     * @param item  hash键
     * @param delta 值
     * @return
     */
    public double hashIncrOeDecr(String key, String item, double delta) {
        return redisTemplate.opsForHash().increment(key, item, delta);
    }


    // zset

    /**
     * 向指定key中添加元素，按照score值由小到大进行排列
     *
     * @param key   键值
     * @param value 值
     * @param score 分数
     */
    public void zAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加zset
     *
     * @param key 键值
     * @param set TypedTuple
     */
    public void zMAdd(String key, Set<ZSetOperations.TypedTuple<Object>> set) {
        redisTemplate.opsForZSet().add(key, set);
    }

    /**
     * 删除元素 zRem
     *
     * @param key   键值
     * @param value 值
     */
    public void zRemove(String key, Object value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 增加key对应的集合中元素v1的score值，并返回增加后的值 incrementScore
     *
     * @param key   键值
     * @param value 值
     * @param score 分数
     */
    public Double zIncrScore(String key, Object value, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 查询value对应的score   zScore
     *
     * @param key    键值
     * @param object 元素
     * @return
     */
    public Double zScore(String key, Object object) {
        return redisTemplate.opsForZSet().score(key, object);
    }

    /**
     * 判断value在zset中的倒序排名  reverseRank
     * 从大到小
     *
     * @param key 键值
     * @return
     */
    public Long zReverseRank(String key, Object object) {
        return redisTemplate.opsForZSet().reverseRank(key, object);
    }

    /**
     * 查询集合中指定顺序的值， 0 -1 表示获取全部的集合内容  zrange
     * 返回有序的集合，score小的在前面
     *
     * @param key   键值
     * @param start 开始下标
     * @param end   结束下标
     * @return
     */
    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 查询集合中指定顺序的值和score，0, -1 表示获取全部的集合内容
     *
     * @param key   键值
     * @param start 开始下标
     * @param end   结束下标
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangWithScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 根据id的区间查询的值
     *
     * @param key   键值
     * @param start 开始下标
     * @param end   结束下标
     * @return
     */
    public Set<Object> zRevRangeById(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 根据score的值，来获取满足条件的从高到底的集合
     *
     * @param key 键值
     * @param min 最小score
     * @param max 醉倒score
     * @return
     */
    public Set<Object> zRevRangeByScore(String key, long min, long max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 返回集合的长度
     *
     * @param key 键值
     * @return
     */
    public Long zSize(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }
}

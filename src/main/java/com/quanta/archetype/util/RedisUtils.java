package com.quanta.archetype.util;

import com.quanta.archetype.constant.RedisKeyTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * redis工具类
 *
 * @author quanta
 * @author Linine
 * @version 1.1
 * @since 2021/11/25
 */
@Slf4j
@Component // 实现bean的注入
@SuppressWarnings("ConstantConditions")
public class RedisUtils {

    private final Pattern pattern = Pattern.compile("\\{[0-9]\\}");

    @Autowired
    @Qualifier("redisTemplate") //自定义缓存配置类
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * redis键生成器
     * 防止参数为空导致key生成不合法
     *
     * @param template 生成key模板
     * @param arg/args 参数
     * @return redisKey
     * @since 1.2
     */
    public String generateKey(RedisKeyTemplates template, String arg) {
        if (!StringUtils.hasText(arg)) throw new IllegalArgumentException("arg must be non blank!");
        return template.getTemplate().replace("{0}", arg);
    }

    public String generateKey(RedisKeyTemplates template, String[] args) {
        if (args == null) throw new IllegalArgumentException("Argument count not match template");
        String result = template.getTemplate();
        Matcher matcher = pattern.matcher(result);
        int argsCount = 0;
        while (matcher.find()) {
            argsCount++;
        }
        if (argsCount != args.length) throw new IllegalArgumentException("Argument count not match template");
        for (int i = 0; i < args.length; i++) {
            if (!StringUtils.hasText(args[i])) throw new IllegalArgumentException("Argument must be non blank!");
            result = result.replace("{" + i + "}", args[i]);
        }
        return result;
    }

    /**
     * 是否存在key
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 根据正则表达式获得键集合
     */
    public Set<String> scan(String pattern) {
        Assert.hasText(pattern, "Pattern must not be empty");
        try (Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions().match(pattern).count(10000).build())) {
            return StreamSupport.stream(cursor.spliterator(), false)
                    .collect(Collectors.toSet());
        }

    }

    /*
    String数据类型
     */


    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    public void set(String key, Object value, long time, TimeUnit timeUnit) {
        if (time < 0) throw new IllegalArgumentException("redis cache expiration time cannot be negative");
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public List<Object> mGet(List<String> keys) {
        if (keys != null && !keys.isEmpty()) return redisTemplate.opsForValue().multiGet(keys);
        return Collections.emptyList();
    }

    public boolean mSet(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            redisTemplate.opsForValue().multiSet(map);
            return true;
        }
        return false;
    }

    /**
     * 新增键不存在则写入返回true,已存在返回false
     */
    public boolean setIfAbsent(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * 新增键不存在则写入返回true,已存在返回false
     */
    public boolean setIfAbsent(String key, Object value, long time, TimeUnit timeUnit) {
        if (time < 0) throw new IllegalArgumentException("redis cache expiration time cannot be negative");
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit));
    }

    /**
     * 当key集合整个不存在再置入返回true，已存在key返回false
     *
     * @param map 键值对
     * @return 置入状态
     */
    public boolean mSetIfAbsent(Map<String, Object> map) {
        if (map != null && !map.isEmpty())
            return Boolean.TRUE.equals(redisTemplate.opsForValue().multiSetIfAbsent(map));
        return false;
    }


    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) redisTemplate.delete(key[0]);
            else redisTemplate.delete(Arrays.asList(key));
        }
    }

    public void del(List<String> keys) {
        if (keys != null && !keys.isEmpty()) redisTemplate.delete(keys);
    }

    /**
     * 获得键剩余时间
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().getOperations().getExpire(key, timeUnit);
    }

    /**
     * 设置键过期时间
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        if (time < 0) throw new IllegalArgumentException("redis cache expiration time cannot be negative");
        return Boolean.TRUE.equals(redisTemplate.expire(key, time, timeUnit));
    }

    /**
     * 获得类型为int的键值
     * 不存在则返回0
     */
    public int getIntOrZero(String key) {
        return redisTemplate.opsForValue().get(key) == null ? 0 : (int) redisTemplate.opsForValue().get(key);
    }

    /**
     * 键值增加
     *
     * @param key   键
     * @param delta 增值
     * @return 增加后的值
     */
    public long incr(String key, long delta) {
        if (delta < 0) throw new IllegalArgumentException("Incr factor must greater than zero.");
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 键值自增
     *
     * @param key 键
     * @return 增加后的值
     */
    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 键值减少
     *
     * @param key   键
     * @param delta 降值
     * @return 减少后的值
     */
    public long decr(String key, long delta) {
        if (delta < 0) throw new IllegalArgumentException("Decr factor must greater than zero.");
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 键值自减
     *
     * @param key 键
     * @return 减少后的值
     */
    public long decr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }


    /*
    hashMap数据类型
     */

    /**
     * hash获得值
     *
     * @param key  键
     * @param item hash键
     */
    public Object hashGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * hash批量获得值
     *
     * @param key 键
     */
    public Map<Object, Object> hashMGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * hash单条插入
     *
     * @param key   键
     * @param item  hash键
     * @param value 值
     */
    public void hashSet(String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
    }

    public void hashSet(String key, String item, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, item, value);
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * hash批量添加值
     *
     * @param key 键
     * @param map hashKey和hashValue
     */
    public void hashMSet(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * hash批量添加值，同时设置过期时间
     *
     * @param key  键
     * @param map  hashKey和hashValue
     * @param time 过期时间
     */
    public void hashMSet(String key, Map<String, Object> map, long time, TimeUnit timeUnit) {
        if (time < 0) throw new IllegalArgumentException("redis cache expiration time cannot be negative");
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, time, timeUnit);

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
     * @param item hash键
     */
    public boolean hashHasKey(String key, String item) {
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, item));
    }

    /**
     * hash的hash键值增加或减少
     *
     * @param key   键
     * @param item  hash键
     * @param delta 值
     */
    public double hashIncrOeDecr(String key, String item, double delta) {
        return redisTemplate.opsForHash().increment(key, item, delta);
    }


    /*
    ZSet数据结构
     */

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
     * @return 增量后的最新分数
     */
    public Double zIncrScore(String key, Object value, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 查询value对应的score   zScore
     *
     * @param key   键值
     * @param value 元素
     * @return
     */
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 判断value在zset中的正序排名 rank
     * 从小到大
     *
     * @param key
     * @param object
     * @return
     */
    public Long zRank(String key, Object object) {
        return redisTemplate.opsForZSet().rank(key, object);
    }

    /**
     * 判断value在zset中的倒序排名  reverseRank
     * 从大到小
     *
     * @param key    键值
     * @param object
     * @return
     */
    public Long zReverseRank(String key, Object object) {
        return redisTemplate.opsForZSet().reverseRank(key, object);
    }

    /**
     * 查询集合中指定顺序的值，
     * 0 -1 表示获取全部的集合内容  zrange
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
     * 查询集合中指定顺序的值和score
     * 0, -1 表示获取全部的集合内容
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


    /*
    set数据类型
     */
    public void lrSet(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void lrSet(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForList().rightPush(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public <T> void lrSet(String key, Collection<T> collection) {
        redisTemplate.opsForList().rightPushAll(key, collection);
    }

    public <T> void lrSet(String key, Collection<T> collection, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForList().rightPushAll(key, collection);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public List<Object> lrRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public void lrDelete(String key, long count, Object value) {
        redisTemplate.opsForList().remove(key, count, value);
    }
}

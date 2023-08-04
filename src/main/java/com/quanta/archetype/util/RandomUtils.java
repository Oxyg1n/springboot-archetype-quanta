package com.quanta.archetype.util;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 随机工具类
 *
 * @author Linine
 * @since 2023/3/17 22:18
 */
public class RandomUtils {

    private static final List<String> adj;
    private static final List<String> noun;

    static {
        adj = Stream.of("漂亮", "温柔", "善良", "可爱", "美丽", "纯净", "朴实", "纯真", "执着", "坚强", "勇敢", "帅气", "大方", "英俊",
                "王者", "坚毅", "实干", "吃苦", "耐劳", "大气", "风度", "平和", "高深", "智慧", "睿智", "刚强", "刚毅", "厚重").collect(Collectors.toList());
        noun = Stream.of("小明", "小红", "小蓝", "小白", "小黑").collect(Collectors.toList());
    }

    /**
     * 生成随机用户别名
     *
     * @return 用户别名
     */
    public static String getRandomName() {
        Random random = new Random();
        return adj.get(random.nextInt(adj.size())) + "的" + noun.get(random.nextInt(noun.size()));
    }
}

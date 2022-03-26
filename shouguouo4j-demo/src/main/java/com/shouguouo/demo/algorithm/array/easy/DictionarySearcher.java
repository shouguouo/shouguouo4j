package com.shouguouo.demo.algorithm.array.easy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字典查找的算法。
 * input:
 * 1. input_file
 * 每一行有一个词汇，如“浙江”, “浙江大学”, “美国”, “美国政府”。该文件可能有100万词
 * 2. a document，字符串。一般有2000字左右。如 “美国规划协会中国办公室揭牌仪式及美国规划领域合作研讨会在浙江大学城乡规划设计研究院208会议室举行。美国规划协会CEO James Drinan，国际项目及外联主任Jeffrey Soule先生，浙江大学党委副书记任少波，浙江大学控股集团领导杨其和，西湖区政府代表应权英副主任....”
 *
 * output:
 * 输出document中出现的词汇,以及其位置列表。如
 * {
 * “美国” : [ 0,16, ....],
 * “浙江”: [28, ...]
 * “浙江大学”: [28, ...]
 * }
 *
 * @author shouguouo
 * @date 2022-02-14 19:15:58
 */
public class DictionarySearcher {

    private final Set<String> wordSet;

    public DictionarySearcher(String filename) throws FileNotFoundException {
        if (filename == null || filename.isEmpty()) {
            throw new RuntimeException("filename must not be empty");
        }
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        // 若文件过大无法全部加载进JVM，考虑使用其他缓存策略：如Redis或者其他带持久化机制的中间件
        wordSet = reader.lines().collect(Collectors.toSet());
    }

    public static void main(String[] args) throws FileNotFoundException {
        DictionarySearcher searcher = new DictionarySearcher(DictionarySearcher.class.getResource("/input_file").getPath());
        HashMap<String, ArrayList<Integer>> result = searcher.search("美国规划协会中国办公室揭牌仪式及美国规划领域合作研讨会在浙江大学城乡规划设计研究院208会议室举行。美国规划协会CEO James Drinan，国际项目及外联主任Jeffrey Soule先生，浙江大学党委副书记任少波，浙江大学控股集团领导杨其和，西湖区政府代表应权英副主任....");
        result.forEach((key, value) -> System.out.println(key + ":" + value));
    }

    /**
     * 简易实现
     * 成熟可使用Lucene等中间件
     *
     * @param document 文档
     * @return 统计信息
     */
    public HashMap<String, ArrayList<Integer>> search(String document) {
        HashMap<String, ArrayList<Integer>> res = new HashMap<>(16);
        for (int left = 0; left < document.length(); left++) {
            for (int right = document.length(); right >= left; right--) {
                String current = document.substring(left, right);
                if (wordSet.contains(current)) {
                    int position = left;
                    res.compute(current, (k, v) -> {
                        if (v == null) {
                            v = new ArrayList<>();
                        }
                        v.add(position);
                        return v;
                    });
                }
            }
        }
        return res;
    }

}

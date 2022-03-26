package com.shouguouo.demo.algorithm.array.easy;

import java.util.ArrayList;
import java.util.List;

/**
 * 穷尽集合之间的元素组合。
 * input: list of list
 * [ [‘a’, ‘b’, ‘c’], [‘1’, ‘2’, ‘3’, ‘4’, ‘5’, ‘6’] , [‘A’, ‘B’, ‘C’, ‘D’] ]
 * output:
 * [
 * [‘a’, ‘1’, ‘A’],
 * [‘a’, ‘1’, ‘B’],
 * [‘a’, ‘1’, ‘C’],
 * [‘a’, ‘1’, ‘D’],
 * ...
 * [‘c’, ‘6’, ‘D’]
 * ]
 * 实现下面的方法, 要求不要用递归的方法。
 * class CombinationProblem {
 *
 *
 *
 * }
 *
 * @author shouguouo
 * @date 2022-02-14 18:40:23
 */
public class CombinationProblem {

    public static void main(String[] args) {
        ArrayList<ArrayList<String>> input = new ArrayList<ArrayList<String>>() {{
            ArrayList<String> first = new ArrayList<>();
            first.add("a");
            first.add("b");
            first.add("c");
            add(first);
            ArrayList<String> second = new ArrayList<>();
            second.add("1");
            second.add("2");
            second.add("3");
            second.add("4");
            second.add("5");
            second.add("6");
            add(second);
            ArrayList<String> third = new ArrayList<>();
            third.add("A");
            third.add("B");
            third.add("C");
            third.add("D");
            add(third);
        }};
        List<ArrayList<String>> combination = new CombinationProblem().getSetCombination(input);
        combination.forEach(System.out::println);
        System.out.println(combination.size());
    }

    public ArrayList<ArrayList<String>> getSetCombination(ArrayList<ArrayList<String>> input) {
        // 排除无效输入
        if (input == null || input.isEmpty()) {
            return new ArrayList<>();
        }
        input.removeIf(x -> x == null || x.isEmpty());
        if (input.isEmpty()) {
            return new ArrayList<>();
        }
        // 计算组合总数
        int total = input.stream().mapToInt(ArrayList::size).reduce(1, (x, y) -> x * y);
        // 遍历指针
        int[] points = new int[input.size()];
        // 当前后续遍历指针
        int currBackPoint = input.size() - 1;
        ArrayList<ArrayList<String>> res = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            ArrayList<String> comb = new ArrayList<>(input.size());
            for (int j = 0; j < points.length; j++) {
                comb.add(input.get(j).get(points[j]));
            }
            res.add(comb);
            // 构建下次遍历的points状态
            for (int j = points.length - 1; j >= 0; j--) {
                if (points[j] < input.get(j).size() - 1) {
                    currBackPoint = Math.min(j, currBackPoint);
                    // 重置更新节点后的状态
                    for (int k = j + 1; k < points.length; k++) {
                        points[k] = 0;
                    }
                    points[j]++;
                    break;
                }
            }
        }
        return res;
    }

}

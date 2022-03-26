package com.shouguouo.demo.algorithm.array.easy;

import java.util.Arrays;

/**
 * @author shouguouo
 * @date 2022-02-15 09:14:38
 */
public class SortedSquares {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(new SortedSquares().sortedSquares(new int[] { -10000, -9999, -7, -5, 0, 0, 10000 })));
    }

    public int[] sortedSquares(int[] nums) {
        if (nums[0] >= 0) {
            for (int i = 0; i < nums.length; i++) {
                nums[i] = nums[i] * nums[i];
            }
            return nums;
        }
        if (nums[nums.length - 1] <= 0) {
            int[] res = new int[nums.length];
            for (int i = 0; i < nums.length; i++) {
                res[i] = nums[nums.length - 1 - i] * nums[nums.length - 1 - i];
            }
            return res;
        }
        // 获取分割点
        int left = 0;
        int right = nums.length - 1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < 0 && nums[i + 1] >= 0) {
                left = i;
                right = i + 1;
            }
        }
        int[] res = new int[nums.length];
        int index = 0;
        while (left >= 0 || right <= nums.length - 1) {
            if (left < 0) {
                res[index++] = nums[right] * nums[right];
                right++;
                continue;
            }
            int leftSquare = nums[left] * nums[left];
            int rightSquare = nums[right] * nums[right];
            if (leftSquare >= rightSquare) {
                res[index++] = rightSquare;
                right++;
            } else {
                res[index++] = leftSquare;
                left--;
            }
        }
        return res;
    }
}

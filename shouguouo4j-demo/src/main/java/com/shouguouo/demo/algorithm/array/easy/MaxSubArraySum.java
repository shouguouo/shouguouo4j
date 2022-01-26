package com.shouguouo.demo.algorithm.array.easy;

/**
 * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 *
 * 子数组 是数组中的一个连续部分。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
 * 示例 2：
 *
 * 输入：nums = [1]
 * 输出：1
 * 示例 3：
 *
 * 输入：nums = [5,4,-1,7,8]
 * 输出：23
 *  
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * -104 <= nums[i] <= 104
 *  
 *
 * 进阶：如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的 分治法 求解。
 *
 * @author shouguouo
 * @date 2022-01-26 20:51:31
 */
public class MaxSubArraySum {

    public static void main(String[] args) {
        System.out.println(new MaxSubArraySum().maxSubArray(new int[] { -2, 1, -3, 4, -1, 2, 1, -5, 4 }));
        System.out.println(new MaxSubArraySum().maxSubArray(new int[] { 1 }));
        System.out.println(new MaxSubArraySum().maxSubArray(new int[] { 5, 4, -1, 7, 8 }));
    }

    public int maxSubArray(int[] nums) {
        // 1.定义dp数组及下标含义：dp[i]表示包含下标i之前的最大连续子序列和
        // 2.dp[i]推导：dp[i] = max(dp[i - 1] + nums[i], nums[i])
        // 3.dp数组初始化：dp[0] = nums[0]
        // 4.确定遍历顺序：从前往后
        // 结果为动态方程中的最大值 res = max(res, dp[i]);
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        int res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
            res = Math.max(dp[i], res);
        }
        return res;
    }
}

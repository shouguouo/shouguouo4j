package com.shouguouo.common.util;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author shouguouo
 * @date 2021-07-16 10:25:07
 */
@UtilityClass
public class VariationUtils {

    public final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    /**
     * 时间线
     */
    private final Range<String> TIMELINE = new Range<>("000000", "999999", 0);

    /**
     * 计算可变信息
     *
     * @param unsortedRangeList 乱序的区间列表
     * @return 包含整个时间线的区间，通过paymentState区分是否正常缴费
     */
    public List<Range<String>> calcVariation(List<Range<String>> unsortedRangeList) {
        List<Range<String>> mergedValidVariation = mergeVariation(unsortedRangeList);
        List<Range<String>> invalidVariation = cutVariationByTimeline(mergedValidVariation);
        return Stream.of(mergedValidVariation, invalidVariation).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 合并有效区间
     *
     * @param validVariationList 有效区间列表
     * @return 合并后的有效区间
     */
    private List<Range<String>> mergeVariation(List<Range<String>> validVariationList) {
        if (validVariationList == null || validVariationList.isEmpty()) {
            return Collections.emptyList();
        }
        validVariationList.sort(Comparator.comparing(Range::getStart));

        List<Range<String>> mergedPeriod = Lists.newArrayList(validVariationList.get(0));
        Range<String> lastPeriod = validVariationList.get(0);
        for (int i = 1; i < validVariationList.size(); i++) {
            Range<String> currPeriod = validVariationList.get(i);
            if (currPeriod.start.compareTo(lastPeriod.end) <= 0) {
                if (currPeriod.end.compareTo(lastPeriod.end) > 0) {
                    lastPeriod.end = currPeriod.end;
                }
                continue;
            }
            mergedPeriod.add(currPeriod);
            lastPeriod = currPeriod;
        }
        mergedPeriod.forEach(period -> period.setPaymentState(1));
        return mergedPeriod;
    }

    /**
     * 根据时间线和有效区间截取无效区间
     *
     * @param mergedRange 合并后的有效区间
     * @return 无效区间列表
     */
    private List<Range<String>> cutVariationByTimeline(final List<Range<String>> mergedRange) {
        if (mergedRange == null || mergedRange.isEmpty()) {
            return Lists.newArrayList(TIMELINE);
        }
        List<Range<String>> differencePeriod = new ArrayList<>();
        mergedRange.sort(Comparator.comparing(Range::getStart));
        Range<String> lastPeriod = mergedRange.get(0);
        differencePeriod.add(new Range<>(TIMELINE.start, minusMonth(mergedRange.get(0).start)));
        for (Range<String> currPeriod : mergedRange) {
            if (currPeriod.start.compareTo(lastPeriod.end) > 0) {
                differencePeriod.add(new Range<>(plusMonth(lastPeriod.end), minusMonth(currPeriod.start)));
                lastPeriod = currPeriod;
            }
        }
        differencePeriod.add(new Range<>(plusMonth(mergedRange.get(mergedRange.size() - 1).end), TIMELINE.end));
        differencePeriod.forEach(period -> period.setPaymentState(0));
        return differencePeriod;
    }

    private String plusMonth(String str) {
        return plusMonths(str, 1);
    }

    private String minusMonth(String str) {
        return plusMonths(str, -1);
    }

    public String plusMonths(String str, long monthsToAdd) {
        int parsed = Integer.parseInt(str);
        return YearMonth.of(parsed / 100, parsed % 100)
                .plusMonths(monthsToAdd)
                .format(YEAR_MONTH_FORMATTER);
    }

    public static void main(String[] args) {
        System.out.println(calcVariation(Lists.newArrayList(
                new Range<>("202101", "202102")
        )));
        System.out.println(calcVariation(Lists.newArrayList(
                new Range<>("202101", "202101")
        )));
        System.out.println(calcVariation(Lists.newArrayList(
        )));
        System.out.println(calcVariation(Lists.newArrayList(
                new Range<>("202103", "202105"),
                new Range<>("202104", "202106"),
                new Range<>("202108", "202109")
        )));
        System.out.println(System.currentTimeMillis());
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Range<T extends Comparable<? super T>> {

        @NonNull
        private T start;

        @NonNull
        private T end;

        private int paymentState;
    }
}

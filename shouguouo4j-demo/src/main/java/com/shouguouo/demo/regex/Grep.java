package com.shouguouo.demo.regex;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 命令行参数:
 * -i -1 system ./shouguouo4j-demo/src/main/java/com/shouguouo/demo/regex/Grep.java ./shouguouo4j-demo/src/main/java/com/shouguouo/demo/regex/RegexAppend.java ./shouguouo4j-demo/src/main/java/com/shouguouo/demo/regex/RegexReplace.java
 *
 * @author shouguouo
 * @date 2022-04-10 21:11:08
 */
public class Grep {

    private final Pattern pattern;

    public Grep(Pattern pattern) {
        this.pattern = pattern;
    }

    public Grep(String regex) {
        this(regex, false);
    }

    public Grep(String regex, boolean ignoreCase) {
        this.pattern = Pattern.compile(regex, ignoreCase ? Pattern.CASE_INSENSITIVE : 0);
    }

    public static void main(String[] args) throws IOException {
        // 设置默认值
        boolean ignoreCase = false;
        boolean onebyone = false;
        // 采集变量
        List<String> argList = new LinkedList<>();
        // 循环遍历变量，查找转换并保存模式及文件名
        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (arg.equals("-i") || arg.equals("--ignore-case")) {
                    ignoreCase = true;
                }
                if (arg.equals("-1")) {
                    onebyone = true;
                }
                continue;
            }
            // 不是switch，将其添加到列表中
            argList.add(arg);
        }
        // 是否有足够的变量可以运行？
        if (argList.size() < 2) {
            System.err.println("usage: [options] pattern filename ...");
            return;
        }
        // 列表中第一个变量将被作为正则模式。
        // 将模式及忽略大小写标志的当前值传递给新的Grep对象。
        Grep grepper = new Grep(argList.remove(0), ignoreCase);
        // 随意点，拆分成调用grep程序和打印结果两种方式
        if (onebyone) {
            // 循环遍历文件名并用grep处理它们
            for (String fileName : argList) {
                // 在每次grep前先打印文件名
                System.out.println(fileName + ":");
                MatchedLine[] matches = null;
                // 捕获异常
                try {
                    matches = grepper.grep(fileName);
                } catch (IOException e) {
                    System.err.println("\t*** " + e);
                    continue;
                }
                // 打印匹配行
                for (MatchedLine match : matches) {
                    System.out.println(" "
                            + match.getLineNumber()
                            + " [" + match.getStart()
                            + "-" + (match.getEnd() - 1)
                            + "]: "
                            + match.getLineText());
                }
            }
        } else {
            // 把文件名列表转换到File阵列中
            File[] files = new File[argList.size()];
            for (int i = 0; i < files.length; i++) {
                files[i] = new File(argList.get(i));
            }
            // 运行grep程序；忽略无法读取的文件
            MatchedLine[] matches = grepper.grep(files);
            // 打印匹配行的资料
            for (MatchedLine match : matches) {
                System.out.println(match.getFile().getName()
                        + ", " + match.getLineNumber() + ": "
                        + match.getLineText());
            }
        }

    }

    public MatchedLine[] grep(String fileName) throws IOException {
        return grep(new File(fileName));
    }

    public MatchedLine[] grep(File file) throws IOException {
        return grepList(file).toArray(new MatchedLine[0]);
    }

    public MatchedLine[] grep(File[] files) throws IOException {
        List<MatchedLine> aggregate = new LinkedList<>();
        for (File file : files) {
            aggregate.addAll(grepList(file));
        }
        return aggregate.toArray(new MatchedLine[0]);
    }

    private List<MatchedLine> grepList(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("Does not exist: " + file);
        }
        if (!file.isFile()) {
            throw new IOException("Not a regular file: " + file);
        }
        if (!file.canRead()) {
            throw new IOException("Unreadable file: " + file);
        }
        LinkedList<MatchedLine> list = new LinkedList<>();
        try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
            Matcher matcher = this.pattern.matcher("");
            String line;
            while ((line = reader.readLine()) != null) {
                // 重置匹配的内容
                matcher.reset(line);
                if (matcher.find()) {
                    list.add(new MatchedLine(file, reader.getLineNumber(), line, matcher.start(), matcher.end()));
                }
            }
        }
        return list;
    }

    @Getter
    @AllArgsConstructor
    public static class MatchedLine {

        private final File file;

        private final int lineNumber;

        private final String lineText;

        private final int start;

        private final int end;
    }
}

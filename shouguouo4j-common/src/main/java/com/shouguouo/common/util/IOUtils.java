package com.shouguouo.common.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shouguouo
 * @date 2022-03-27 21:43:44
 */
@UtilityClass
public class IOUtils {

    public static List<String> gatherFileAbsolutePath(File file) {
        List<File> res = new ArrayList<>();
        gatherFile(file, res);
        return res.stream().map(File::getAbsolutePath).collect(Collectors.toList());
    }

    private void gatherFile(File file, List<File> res) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            res.add(file);
            return;
        }
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(f -> gatherFile(f, res));
        }
    }

}

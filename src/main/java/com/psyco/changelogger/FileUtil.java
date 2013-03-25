package com.psyco.changelogger;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {

    public static Set<File> getAllFiles(File dir) {
        Set<File> list = new HashSet<File>();
        getAllFiles(dir, list);
        return list;
    }

    private static Set<File> getAllFiles(File dir, Set<File> list) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                getAllFiles(file, list);
            } else {
                list.add(file);
            }
        }
        return list;
    }

    public static Set<File> getAllFiles(File dir, FilenameFilter filter) {
        Set<File> list = new HashSet<File>();
        getAllFiles(dir, filter, list);
        return list;
    }

    private static Set<File> getAllFiles(File dir, FilenameFilter filter, Set<File> list) {
        for (File file : dir.listFiles(filter)) {
            if (file.isDirectory()) {
                getAllFiles(file, filter, list);
            } else {
                list.add(file);
            }
        }
        return list;
    }

}

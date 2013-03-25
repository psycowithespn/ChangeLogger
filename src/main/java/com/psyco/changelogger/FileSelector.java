package com.psyco.changelogger;

import org.bukkit.configuration.Configuration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

public class FileSelector implements FilenameFilter {

    private List<String> acceptedDirs;
    private List<String> acceptedFiles;
    private boolean parentIncluded;
    private final File parent;

    public FileSelector(Configuration config, File parent) {
        this.parent = parent;
        config.addDefault("include-plugins", true);
        parentIncluded = config.getBoolean("include-plugins");
        acceptedDirs = config.getStringList("accepted-directories");
        acceptedFiles = config.getStringList("accepted-files");
    }

    @Override
    public boolean accept(File dir, String name) {
        try {
            if (dir.getCanonicalFile().equals(parent.getCanonicalFile()) && parentIncluded) {
                return true;
            }
            if (acceptedDirs != null) {
                String path = dir.getCanonicalPath().replace(parent.getCanonicalPath(), "");
                path = path.substring(1);
                if (acceptedDirs.contains(path)) {
                    return true;
                }
            }
            if (acceptedFiles != null) {
                String filePath = new File(dir, name).getCanonicalPath().replace(parent.getCanonicalPath(), "").substring(1);
                if (acceptedFiles.contains(filePath)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

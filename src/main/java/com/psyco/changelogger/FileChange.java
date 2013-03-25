package com.psyco.changelogger;

import java.io.File;

public class FileChange {

    private File file;
    private FileChangeReason reason;
    private String relConfigPath;

    public FileChange(File file, String relConfigPath, FileChangeReason reason) {
        this.file = file;
        this.relConfigPath = relConfigPath;
        this.reason = reason;
    }

    public File getFile() {
        return file;
    }

    public FileChangeReason getReason() {
        return reason;
    }

    public String getRelConfigPath() {
        return relConfigPath;
    }
}

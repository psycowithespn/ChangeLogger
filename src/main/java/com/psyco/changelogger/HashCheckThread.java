package com.psyco.changelogger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HashCheckThread implements Runnable {

    private File parent;
    private Map<String, Object> hashes;
    private Map<String, String> allHashes = new HashMap<String, String>();
    private final FileSelector selector;

    public HashCheckThread(File parent, Map<String, Object> previousHashes, FileSelector selector) {
        this.parent = parent;
        this.hashes = previousHashes;
        this.selector = selector;
    }

    @Override
    public void run() {
        //Generate file list
        Set<File> files = FileUtil.getAllFiles(parent, selector);
        //MD5 hash and add to set of FileChanges if different than previous
        Set<FileChange> changes = new HashSet<FileChange>();
        String parentPath = null;
        try {
            parentPath = parent.getCanonicalPath() + File.separatorChar;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (File file : files) {
            String md5 = null;
            String fileName = null;
            try {
                md5 = MD5Util.getMD5(file);
                fileName = file.getCanonicalPath();
            } catch (IOException e) {
            }
            if (md5 == null || fileName == null) {
                System.err.println("[ChangeLogger] Failed to get md5 of a file");
                continue;
            }
            fileName = fileName.replace(parentPath, "");
            allHashes.put(fileName, md5);
            if (hashes.containsKey(fileName)) {
                String hash = (String) hashes.get(fileName);
                if (!hash.contains(md5)) {
                    changes.add(new FileChange(file, fileName, FileChangeReason.CHANGED));
                }
                hashes.remove(fileName);
            } else {
                changes.add(new FileChange(file, fileName, FileChangeReason.ADDED));
            }
        }
        for (String name : hashes.keySet()) {
            File del = new File(parent, name);
            if (del.exists()) {
                continue;
            }
            changes.add(new FileChange(del, name, FileChangeReason.DELETED));
        }

        ChangeLogger.getInstance().finishedHashing(allHashes, changes);
    }
}

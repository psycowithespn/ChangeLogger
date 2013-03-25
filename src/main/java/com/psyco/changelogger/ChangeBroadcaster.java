package com.psyco.changelogger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

import java.util.Set;

public class ChangeBroadcaster implements Runnable {

    private Set<FileChange> changes;
    private Configuration config;

    public ChangeBroadcaster(Set<FileChange> changes, Configuration config) {
        this.changes = changes;
        this.config = config;
    }

    @Override
    public void run() {
        for (FileChange change : changes) {
            Bukkit.broadcastMessage(getChatColor(change.getReason()) + change.getRelConfigPath() + " " + change.getReason().toString().toLowerCase());
        }
    }

    public ChatColor getChatColor(FileChangeReason reason) {
        switch (reason) {
            case ADDED:
                return ChatColor.GREEN;
            case CHANGED:
                return ChatColor.RED;
            case DELETED:
                return ChatColor.DARK_RED;
        }
        return ChatColor.BLUE;
    }
}

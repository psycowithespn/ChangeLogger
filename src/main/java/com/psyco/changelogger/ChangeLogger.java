package com.psyco.changelogger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ChangeLogger extends JavaPlugin {

    private static ChangeLogger instance;
    private BukkitTask task;
    private YamlConfiguration dataConfig = new YamlConfiguration();
    private File dataFile;
    private ConfigurationSection hashSection;

    public static ChangeLogger getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        File plugins = getDataFolder().getParentFile();
        dataConfig.options().pathSeparator(File.separatorChar);
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            getDataFolder().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            dataConfig.load(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        if (dataConfig.isConfigurationSection("md5")) {
            hashSection = dataConfig.getConfigurationSection("md5");
        } else {
            hashSection = dataConfig.createSection("md5");
        }
        getLogger().info("Starting hashing...");
        startHashing(plugins, hashSection);
    }

    public void onDisable() {
        if (task != null) {
            task.cancel();
        }
        instance = null;
    }

    public void broadcastChanges(Set<FileChange> changes) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new ChangeBroadcaster(changes, this.getConfig()));
    }

    public void startHashing(File parent, ConfigurationSection hashes) {
        task = Bukkit.getScheduler().runTaskAsynchronously(this, new HashCheckThread(parent, hashes.getValues(true), new FileSelector(getConfig(), parent)));
    }

    public void finishedHashing(final Map<String, String> allHashes, final Set<FileChange> changes) {
        Bukkit.getScheduler().runTask(this, new Runnable() {
            @Override
            public void run() {
                getLogger().info("Hashing done...");
                task = null;
                hashSection = dataConfig.createSection("md5");
                for (Map.Entry<String, String> entry : allHashes.entrySet()) {
                    hashSection.set(entry.getKey(), entry.getValue());
                }
                try {
                    dataConfig.save(dataFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!getConfig().getBoolean("first-run", true)) {
                    broadcastChanges(changes);
                } else {
                    getConfig().set("first-run", false);
                    saveConfig();
                }
            }
        });

    }
}

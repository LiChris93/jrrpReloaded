package me.lichris93.jrrp.thread;

import me.lichris93.jrrp.values;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static me.lichris93.jrrp.langs.*;
import static me.lichris93.jrrp.values.*;

public class autoSave extends BukkitRunnable {
    private final JavaPlugin plugin;

    public autoSave(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public void run() {
        autoSave_running = true;
        try {
            while (true) {
                values.plugin.saveData();
                Thread.sleep(autosave_interval);
            }
        } catch (Exception e) {
            plugin.getLogger().warning(save_fail);
            e.printStackTrace();
            plugin.getLogger().warning(autoSave_stopped);
            autoSave_running = false;
        }
    }
}

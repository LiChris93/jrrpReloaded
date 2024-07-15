package me.lichris93.jrrp.thread;

import java.io.IOException;

import static me.lichris93.jrrp.langs.*;
import static me.lichris93.jrrp.values.*;

public class autoSave extends Thread {
    public void run() {
        plugin.getLogger().info(save_start_success);
        autoSave_running = true;
        try {
            while (true) {
                plugin.saveData();
                if (interrupted()) {
                    throw new InterruptedException();
                }
                sleep(autosave_interval);
            }
        } catch (IOException e) {
            plugin.getLogger().warning(save_fail);
            e.printStackTrace();
            plugin.getLogger().warning(autoSave_stopped);
            autoSave_running = false;
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning(autoSave_stopped);
            autoSave_running = false;
        }
    }
}

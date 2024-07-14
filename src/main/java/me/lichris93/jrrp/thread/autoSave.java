package me.lichris93.jrrp.thread;

import static me.lichris93.jrrp.values.*;

public class autoSave extends Thread {
    public void run() {
        autoSave_running = true;
        try {
            while (true) {
                plugin.saveData();
                sleep(autosave_interval);
            }
        } catch (Exception e) {
            e.printStackTrace();
            autoSave_running = false;
        }
    }
}

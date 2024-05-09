package me.lichris93.jrrp;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import static me.lichris93.jrrp.values.*;
import static me.lichris93.jrrp.langs.*;

public class autoGC extends BukkitRunnable {//调用info等方法的话要用BukkitRunnable
    private final JavaPlugin plugin;

    public autoGC(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void run() {
        while (true) {
            SimpleDateFormat f = new SimpleDateFormat("MM/dd");//格式化
            String date = f.format(new Date());//日期
            Iterator<Map.Entry<String, String[]>> iterator = DataMap.entrySet().iterator();
            int dataRemoved = 0;
            int millis = 300000;
            while (iterator.hasNext()) {
                Map.Entry<String, String[]> entry = iterator.next();
                if (!entry.getValue()[1].equals(date)) {//删除过期数据
                    iterator.remove();
                    dataRemoved += 1;
                    millis = 86400000;
                }
            }
            if (dataRemoved > 0) {
                plugin.getLogger().info(gc_success.replace("{removed_count}", Integer.toString(dataRemoved)));
            }
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

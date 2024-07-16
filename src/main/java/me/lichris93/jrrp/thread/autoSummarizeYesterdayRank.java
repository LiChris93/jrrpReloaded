package me.lichris93.jrrp.thread;


import java.text.SimpleDateFormat;
import java.util.Date;

import static me.lichris93.jrrp.thread.autoRank.updateRank;
import static me.lichris93.jrrp.langs.*;
import static me.lichris93.jrrp.papi.getNameByRank;
import static me.lichris93.jrrp.papi.getValueByRank;
import static me.lichris93.jrrp.values.*;

public class autoSummarizeYesterdayRank extends Thread {
    public static String[] yesterday_first = {"", ""};
    public static String[] yesterday_second = {"", ""};
    public static String[] yesterday_third = {"", ""};


    public void run() {
        plugin.getLogger().info(sum_start_success);
        int millis = 10000;
        autoSum_running = true;
        try {
            while (true) {
                SimpleDateFormat ftime = new SimpleDateFormat("HH:mm:ss");//格式化
                SimpleDateFormat fdate = new SimpleDateFormat("MM/dd");//格式化
                String date = fdate.format(new Date());
                String[] time = ftime.format(new Date()).split(":");//日期
                boolean time_is_ok = time[0].equals("23") && time[1].equals("59") &&
                        30 <= Integer.parseInt(time[2]) && Integer.parseInt(time[2]) <= 50;//取前一天23:59:30-50的数据，既能保证准确性，又能保证不被GC影响
                if (time_is_ok) {
                    //read
                    updateRank();
                    yesterday_first = new String[]{getNameByRank(1), getValueByRank(1)};
                    yesterday_second = new String[]{getNameByRank(2), getValueByRank(2)};
                    yesterday_third = new String[]{getNameByRank(3), getValueByRank(3)};
                    //broadcast
                    plugin.getServer().broadcastMessage(yesterday_summarized.replace("{date}", date));
                    plugin.getServer().broadcastMessage("§a1:" + yesterday_first[0] + "-" + yesterday_first[1]);
                    plugin.getServer().broadcastMessage("§a2:" + yesterday_second[0] + "-" + yesterday_second[1]);
                    plugin.getServer().broadcastMessage("§a3:" + yesterday_third[0] + "-" + yesterday_third[1]);
                    millis = 86400000;
                }
                if (interrupted()) {
                    throw new InterruptedException();
                }
                sleep(millis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning(thread_stopped_by_exception.replace("{name}","autoSum"));
            autoSum_running = false;
        }
    }
}

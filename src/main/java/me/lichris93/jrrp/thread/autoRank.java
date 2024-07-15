package me.lichris93.jrrp.thread;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.lichris93.jrrp.langs.*;
import static me.lichris93.jrrp.values.*;

public class autoRank extends Thread {
    public static <K, V extends Comparable<? super V>> @NotNull Map<K, V> sortDescend(@NotNull Map<K, V> map) {
        //根据Value降序排列HashMap(来源https://blog.csdn.net/weixin_33446857/article/details/85123772)
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> {
            int compare = (o1.getValue()).compareTo(o2.getValue());
            return -compare;//有负号是降序,反之为升序
        });
        Map<K, V> returnMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }

    public static void updateRank() {
        RankedMap.clear();
        for (Map.Entry<String, String[]> entry : DataMap.entrySet()) {
            RankedMap.put(entry.getKey(), Integer.parseInt(entry.getValue()[0]));
        }
        RankedMap = (HashMap<String, Integer>) sortDescend(RankedMap);//自动生成排名
    }

    public void run() {
        plugin.getLogger().info(rank_start_success);
        autoRank_running = true;
        try {
            while (true) {
                updateRank();
                if (interrupted()) {
                    throw new InterruptedException();
                }
                sleep(60000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().warning(autoRank_stopped);
            autoRank_running = false;
        }
    }
}

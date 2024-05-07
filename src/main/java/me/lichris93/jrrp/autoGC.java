package me.lichris93.jrrp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import static me.lichris93.jrrp.values.*;

public class autoGC extends Thread {
    public void run() {
        while (true) {
            SimpleDateFormat f = new SimpleDateFormat("MM/dd");//格式化
            String date = f.format(new Date());//日期
            Iterator<Map.Entry<String, String[]>> iterator = DataMap.entrySet().iterator();
            int times = 0;
            while (iterator.hasNext()) {
                Map.Entry<String, String[]> entry = iterator.next();
                if (!entry.getValue()[1].equals(date)) {//删除过期数据
                    DataMap.remove(entry.getKey());
                    times += 1;
                }
            }
            if (times != 0) {
                plugin.info("[垃圾回收]已从数组中清除" + times + "条垃圾数据!");
            }
            try {
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package me.lichris93.jrrp;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Charsets;
import me.lichris93.jrrp.thread.autoGC;
import me.lichris93.jrrp.thread.autoRank;
import me.lichris93.jrrp.thread.autoSave;
import me.lichris93.jrrp.thread.autoSummarizeYesterdayRank;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static me.lichris93.jrrp.thread.autoSummarizeYesterdayRank.*;
import static me.lichris93.jrrp.values.*;
import static me.lichris93.jrrp.langs.*;

public final class jrrp extends JavaPlugin {
    @Override
    public void onEnable() {
        //Record how much time does enable this plugin use
        long startLoadingTime = System.currentTimeMillis();
        //Make the values not null
        plugin = this;
        langsFile = new File(getDataFolder(), "langs.yml");//读取langs.yml
        dataFile = new File(getDataFolder(), "data.yml");//读取langs.yml
        autoRank_thread = new autoRank();
        autoGC_thread = new autoGC();
        autoSave_thread = new autoSave();
        autoSum_thread = new autoSummarizeYesterdayRank();
        //Create default yml file when missing
        saveWhenNotExist();
        //Begin Enabling
        info("jrrp is now enabling ——By LiChris93");
        //Load config.yml
        loadConfigOnEnable();
        //Load data.yml
        try {
            loadData();
        } catch (Exception e) {
            plugin.warn(data_read_fail);
            e.printStackTrace();
        }
        //Register game command
        regCommand();
        //register PAPI
        registerPAPI();
        //start GC
        startGC();
        //start AutoRank
        startAutoRank();
        //start AutoSum
        startAutoSum();
        //start AutoSave
        startAutoSave();
        //Finish Enabling
        info(finish_enable.replace("{millis}", Long.toString(System.currentTimeMillis() - startLoadingTime)));
    }

    @Override
    public void onDisable() {
        try {
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        info(on_disable);
    }

    public void saveWhenNotExist() {
        saveDefaultConfig();
        if (!langsFile.exists()) {//若不存在则创建langs.yml(相当于SaveDefaultConfig)
            saveResource("langs.yml", false);
        }
        if (!dataFile.exists()) {//若不存在则创建data.yml(相当于SaveDefaultConfig)
            saveResource("data.yml", false);
        }
    }

    public void regCommand() {
        try {
            Bukkit.getPluginCommand("jrrp").setExecutor(new gameCommand());
            Bukkit.getPluginCommand("jrrp").setTabCompleter(new gameCommand());
            info(reg_command_success);
        } catch (Exception e) {
            warn(reg_command_fail);
            e.printStackTrace();
            //Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void loadConfigOnEnable() {
        try {
            config = plugin.getConfig();//读取config.yml
            langsYML = YamlConfiguration.loadConfiguration(langsFile);
            dataYML = YamlConfiguration.loadConfiguration(dataFile);
            loadConfig();
            info(read_config_success);
        } catch (Exception e) {
            warn(read_config_fail);
            e.printStackTrace();
            //Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void registerPAPI() {
        try {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                new papi(this).register();
                info(papi_success);
            } else {
                warn(no_papi);
            }
        } catch (Exception e) {
            warn(papi_fail);
            e.printStackTrace();
        }
    }

    public void startGC() {
        try {
            autoGC_thread.start();
        } catch (Exception e) {
            warn(gc_start_fail);
            e.printStackTrace();
        }
    }

    public void startAutoRank() {
        try {
            autoRank_thread.start();
        } catch (Exception e) {
            warn(rank_start_fail);
            e.printStackTrace();
        }
    }

    public void startAutoSum() {
        try {
            autoSum_thread.start();
        } catch (Exception e) {
            warn(sum_start_fail);
            e.printStackTrace();
        }
    }

    public void startAutoSave() {
        try {
            if (autosave_enabled) {
                autoSave_thread.start();
            } else {
                warn(save_disabled);
            }
        } catch (Exception e) {
            warn(save_start_fail);
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        autosave_enabled = config.getBoolean("autosave.enabled");
        autosave_interval = config.getInt("autosave.interval");

        clear_all_success = langsYML.getString("lang.clear_all_success");
        clear_specific_player_success = langsYML.getString("lang.clear_specific_player_success");
        clear_specific_player_fail = langsYML.getString("lang.clear_specific_player_fail");
        get_num_success = langsYML.getString("lang.get_num_success");
        get_num_fail = langsYML.getString("lang.get_num_fail");
        generate_success = langsYML.getString("lang.generate_success");
        generate_duplicate = langsYML.getString("lang.generate_duplicate");
        rank_title = langsYML.getString("lang.rank_title");
        rank_format = langsYML.getString("lang.rank_format");
        gc_success = langsYML.getString("lang.gc_success");
        reloaded_success = langsYML.getString("lang.reloaded_success");
        reloaded_fail = langsYML.getString("lang.reloaded_fail");
        yesterday_summarized = langsYML.getString("lang.yesterday_summarized");
        monitor_title = langsYML.getString("lang.monitor_title");
        save_success = langsYML.getString("lang.save_success");
        save_fail = langsYML.getString("lang.save_fail");

        finish_enable = langsYML.getString("lang.finish_enable");
        on_disable = langsYML.getString("lang.on_disable");
        reg_command_success = langsYML.getString("lang.reg_command_success");
        reg_command_fail = langsYML.getString("lang.reg_command_fail");
        read_config_success = langsYML.getString("lang.read_config_success");
        read_config_fail = langsYML.getString("lang.read_config_fail");
        papi_success = langsYML.getString("lang.papi_success");
        no_papi = langsYML.getString("lang.no_papi");
        papi_fail = langsYML.getString("lang.papi_fail");
        gc_start_success = langsYML.getString("lang.gc_start_success");
        gc_start_fail = langsYML.getString("lang.gc_start_fail");
        rank_start_success = langsYML.getString("lang.rank_start_success");
        rank_start_fail = langsYML.getString("lang.rank_start_fail");
        sum_start_success = langsYML.getString("lang.sum_start_success");
        sum_start_fail = langsYML.getString("lang.sum_start_fail");
        save_start_success = langsYML.getString("lang.save_start_success");
        save_start_fail = langsYML.getString("lang.save_start_fail");
        save_disabled = langsYML.getString("lang.save_disabled");

        data_read_success = langsYML.getString("lang.data_read_success");
        data_read_fail = langsYML.getString("lang.data_read_fail");
        data_expired = langsYML.getString("lang.data_expired");

        autoGC_stopped = langsYML.getString("lang.autoGC_stopped");
        autoRank_stopped = langsYML.getString("lang.autoRank_stopped");
        autoSave_stopped = langsYML.getString("lang.autoSave_stopped");
        autoSum_stopped = langsYML.getString("lang.autoSum_stopped");

        thread_already_running = langsYML.getString("lang.thread_already_running");
        thread_already_stopped = langsYML.getString("lang.thread_already_stopped");
        thread_start_success = langsYML.getString("lang.thread_start_success");
        thread_start_fail = langsYML.getString("lang.thread_start_fail");
        thread_stop_success = langsYML.getString("lang.thread_stop_success");
        thread_stop_fail = langsYML.getString("lang.thread_stop_fail");
        thread_not_exist = langsYML.getString("lang.thread_not_exist");

        help_option = langsYML.getString("lang.help_option");
        help_jrrp = langsYML.getString("lang.help_jrrp");
        help_jrrp_help = langsYML.getString("lang.help_jrrp_help");
        help_jrrp_rank = langsYML.getString("lang.help_jrrp_rank");
        help_jrrp_clear = langsYML.getString("lang.help_jrrp_clear");
        help_jrrp_get = langsYML.getString("lang.help_jrrp_get");
        help_jrrp_reload = langsYML.getString("lang.help_jrrp_reload");
        help_jrrp_save = langsYML.getString("lang.help_jrrp_save");
        help_jrrp_monitor = langsYML.getString("lang.help_jrrp_monitor");
        help_jrrp_start = langsYML.getString("lang.help_jrrp_start");
        help_jrrp_stop = langsYML.getString("lang.help_jrrp_stop");
    }

    public static void loadData() throws IOException {
        //从yml读取生json字符串
        String data_today = dataYML.getString("data.today");
        String data_yesterday = dataYML.getString("data.yesterday");
        //解码为json对象
        JSONObject json_today = JSONObject.parseObject(data_today);
        JSONObject json_yesterday = JSONObject.parseObject(data_yesterday);
        //读取json中的信息
        //首先读取日期
        String date_today = dataYML.getString("data.date");
        //获取今天日期
        SimpleDateFormat f = new SimpleDateFormat("MM/dd");
        String real_date = f.format(new Date());
        //实际日期数据中的日期不符,终止读取,并删除数据
        if (!real_date.equals(date_today)) {
            plugin.warn(data_expired);
            dataYML.set("data.date", real_date);
            dataYML.set("data.today", "{\"date\":\"\",\"player\":[],\"rate\":[]}");
            dataYML.set("data.yesterday", "{\"player\":[\"\",\"\",\"\"],\"rate\":[\"\",\"\",\"\"]}");
            dataYML.save(dataFile);
            return;
        }
        //读取今日玩家名和人品值信息
        List<String> player_today = JSON.parseArray(json_today.getJSONArray("player").toJSONString(), String.class);
        List<String> rate_today = JSON.parseArray(json_today.getJSONArray("rate").toJSONString(), String.class);
        //写入变量
        for (int i = 0; i < player_today.size(); i++) {
            DataMap.put(player_today.get(i), new String[]{rate_today.get(i), real_date});
        }
        //读取昨日玩家名和人品值信息
        List<String> player_yesterday = JSON.parseArray(json_yesterday.getJSONArray("player").toJSONString(), String.class);
        List<String> rate_yesterday = JSON.parseArray(json_yesterday.getJSONArray("rate").toJSONString(), String.class);
        //写入变量
        yesterday_first = new String[]{player_yesterday.get(0), rate_yesterday.get(0)};
        yesterday_second = new String[]{player_yesterday.get(1), rate_yesterday.get(1)};
        yesterday_third = new String[]{player_yesterday.get(2), rate_yesterday.get(2)};
        plugin.info(data_read_success);
    }

    public void info(String text) {
        getLogger().info(text);
    }

    public void warn(String text) {
        getLogger().warning(text);
    }

    public void reloadAndGetLang() {//仿照reloadConfig()
        YamlConfiguration newLang = YamlConfiguration.loadConfiguration(langsFile);

        final InputStream defConfigStream = getResource("langs.yml");
        if (defConfigStream == null) {
            return;
        }

        newLang.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        langsYML = newLang;
    }

    public void reloadAndGetData() {//仿照reloadConfig()
        YamlConfiguration newData = YamlConfiguration.loadConfiguration(dataFile);

        final InputStream defConfigStream = getResource("data.yml");
        if (defConfigStream == null) {
            return;
        }

        newData.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        dataYML = newData;
    }

    public void saveData() throws IOException {
        //获取今天日期
        SimpleDateFormat f = new SimpleDateFormat("MM/dd");
        String real_date = f.format(new Date());
        //保存日期
        dataYML.set("data.date", real_date);
        //读取今天数据
        List<String> player_today = new ArrayList<>();
        List<String> rate_today = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : DataMap.entrySet()) {
            player_today.add(entry.getKey());
            rate_today.add(entry.getValue()[0]);
        }
        //保存今天数据
        JSONObject today_json = new JSONObject();//空json
        today_json.put("player", player_today);
        today_json.put("rate", rate_today);
        dataYML.set("data.today", today_json.toString());
        //保存昨天数据
        JSONObject yesterday_json = new JSONObject();//空json
        List<String> player_yesterday = new ArrayList<>();
        List<String> rate_yesterday = new ArrayList<>();

        player_yesterday.add(yesterday_first[0]);
        player_yesterday.add(yesterday_second[0]);
        player_yesterday.add(yesterday_third[0]);

        rate_yesterday.add(yesterday_first[1]);
        rate_yesterday.add(yesterday_second[1]);
        rate_yesterday.add(yesterday_third[1]);

        yesterday_json.put("player", player_yesterday);
        yesterday_json.put("rate", rate_yesterday);
        dataYML.set("data.yesterday", yesterday_json.toString());
        //写入文件
        dataYML.save(dataFile);
    }
}

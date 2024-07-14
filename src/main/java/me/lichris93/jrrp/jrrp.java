package me.lichris93.jrrp;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        //Create default yml file when missing
        saveWhenNotExist();
        //Begin Enabling
        info("jrrp is now enabling ——By LiChris93");
        //Load config.yml
        loadConfigOnEnable();
        //Register game command
        regCommand();
        //register PAPI
        registerPAPI();
        //start GC
        startGC();
        //start AutoRank
        startAutoRank();
        //Finish Enabling
        info(finish_enable.replace("{millis}", Long.toString(System.currentTimeMillis() - startLoadingTime)));
    }

    @Override
    public void onDisable() {
        info(on_disable);
    }

    public void saveWhenNotExist() {
        saveDefaultConfig();
        if (!langsFile.exists()) {//若不存在则创建langs.yml(相当于SaveDefaultConfig)
            saveResource("langs.yml", false);
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
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { // 
                new papi(this).register();
                info(papi_success);
            } else {
                warn(no_papi);
            }
        } catch (Exception e) {
            warn(papi_fail);
            e.printStackTrace();
            //Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void startGC() {
        try {
            new autoGC(this).runTaskAsynchronously(this);
            info(gc_start_success);
        } catch (Exception e) {
            warn(gc_start_fail);
            e.printStackTrace();
        }
    }

    public void startAutoRank() {
        try {
            new autoRank().start();
            new autoSummarizeYesterdayRank(this).runTaskAsynchronously(this);
            info(rank_start_success);
        } catch (Exception e) {
            warn(rank_start_fail);
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
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
        reloaded = langsYML.getString("lang.reloaded");
        yesterday_summarized = langsYML.getString("lang.yesterday_summarized");

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

        help_option = langsYML.getString("lang.help_option");
        help_jrrp = langsYML.getString("lang.help_jrrp");
        help_jrrp_help = langsYML.getString("lang.help_jrrp_help");
        help_jrrp_rank = langsYML.getString("lang.help_jrrp_rank");
        help_jrrp_clear = langsYML.getString("lang.help_jrrp_clear");
        help_jrrp_get = langsYML.getString("lang.help_jrrp_get");
        help_jrrp_reload = langsYML.getString("lang.help_jrrp_reload");
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
}

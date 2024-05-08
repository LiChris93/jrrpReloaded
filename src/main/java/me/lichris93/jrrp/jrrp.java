package me.lichris93.jrrp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static me.lichris93.jrrp.values.*;
import static me.lichris93.jrrp.langs.*;

public final class jrrp extends JavaPlugin {

    @Override
    public void onEnable() {
        //Make the values not null
        plugin = this;
        config = this.getConfig();
        //Create default yml file when missing
        saveDefaultConfig();
        //Begin Enabling
        //Record how much time does enable this plugin use
        long startloadtime = System.currentTimeMillis();
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
        info(finish_enable.replace("{milis}", Long.toString(System.currentTimeMillis() - startloadtime)));

    }

    @Override
    public void onDisable() {
        info(on_disable);
    }

    public void regCommand() {
        try {
            Bukkit.getPluginCommand("jrrp").setExecutor(new gameCommand());
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
            config = plugin.getConfig();
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
            new autoGC().start();
            info(gc_start_success);
        } catch (Exception e) {
            warn(gc_start_fail);
            e.printStackTrace();
        }
    }

    public void startAutoRank() {
        try {
            new autoRank().start();
            info(rank_start_success);
        } catch (Exception e) {
            warn(rank_start_fail);
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        version = config.getString("version");
        clear_all_success = config.getString("lang.clear_all_success");
        clear_specific_player_success = config.getString("lang.clear_specific_player_success");
        clear_specific_player_fail = config.getString("lang.clear_specific_player_fail");
        get_num_success = config.getString("lang.get_num_success");
        get_num_fail = config.getString("lang.get_num_fail");
        generate_success = config.getString("lang.generate_success");
        generate_duplicate = config.getString("lang.generate_duplicate");
        rank_title = config.getString("lang.rank_title");
        rank_format = config.getString("lang.rank_format");
        gc_success = config.getString("lang.gc_success");
        reloaded = config.getString("lang.reloaded");

        finish_enable = config.getString("lang.finish_enable");
        on_disable = config.getString("lang.on_disable");
        reg_command_success = config.getString("lang.reg_command_success");
        reg_command_fail = config.getString("lang.reg_command_fail");
        read_config_success = config.getString("lang.read_config_success");
        read_config_fail = config.getString("lang.read_config_fail");
        papi_success = config.getString("lang.papi_success");
        no_papi = config.getString("lang.no_papi");
        papi_fail = config.getString("lang.papi_fail");
        gc_start_success = config.getString("lang.gc_start_success");
        gc_start_fail = config.getString("lang.gc_start_fail");
        rank_start_success = config.getString("lang.rank_start_success");
        rank_start_fail = config.getString("lang.rank_start_fail");

        help_option = config.getString("lang.help_option");
        help_jrrp = config.getString("lang.help_jrrp");
        help_jrrp_help = config.getString("lang.help_jrrp_help");
        help_jrrp_rank = config.getString("lang.help_jrrp_rank");
        help_jrrp_clear = config.getString("lang.help_jrrp_clear");
        help_jrrp_get = config.getString("lang.help_jrrp_get");
        help_jrrp_reload = config.getString("lang.help_jrrp_reload");
    }

    public void info(String text) {
        getLogger().info(text);
    }

    public void warn(String text) {
        getLogger().warning(text);
    }
}

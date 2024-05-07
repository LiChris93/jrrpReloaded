package me.lichris93.jrrp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static me.lichris93.jrrp.values.*;

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
        //Register game command
        regCommand();
        //Load config.yml
        loadConfig();
        //register PAPI
        registerPAPI();
        //start GC
        startGC();
        //Finish Enabling
        info("jrrp 加载完成!——By LiChris93[" + (System.currentTimeMillis() - startloadtime) + "ms]");

    }

    @Override
    public void onDisable() {
        info("jrrp 关闭完成!——By LiChris93");
    }

    public void regCommand() {
        try {
            Bukkit.getPluginCommand("jrrp").setExecutor(new gameCommand());
            info("命令执行器注册完成");
        } catch (Exception e) {
            warn("jrrp加载失败!原因:命令执行器注册失败,详情请看控制台");
            e.printStackTrace();
            //Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void loadConfig() {
        try {
            version = config.getString("version");
            info("config读取完成");
        } catch (Exception e) {
            warn("jrrp加载失败!原因:config读取失败,详情请看控制台");
            e.printStackTrace();
            //Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void registerPAPI() {
        try {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { // 
                new papi(this).register();
                info("PAPI注册完成");
            } else {
                warn("未找到PAPI,跳过");
            }
        } catch (Exception e) {
            warn("jrrp加载失败!原因:PAPI注册失败,详情请看控制台");
            e.printStackTrace();
            //Disable plugin
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void startGC() {
        try {
            new autoGC().start();
            info("垃圾回收线程启动完成");
        } catch (Exception e) {
            warn("垃圾回收线程启动失败");
        }
    }

    public void info(String text) {
        getLogger().info(text);
    }

    public void warn(String text) {
        getLogger().warning(text);
    }
}

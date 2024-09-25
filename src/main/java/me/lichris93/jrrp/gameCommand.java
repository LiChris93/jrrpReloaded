package me.lichris93.jrrp;

import me.lichris93.jrrp.thread.autoGC;
import me.lichris93.jrrp.thread.autoRank;
import me.lichris93.jrrp.thread.autoSave;
import me.lichris93.jrrp.thread.autoSummarizeYesterdayRank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

import static me.lichris93.jrrp.autoAwardWhenJoin.executeAwardCommand;
import static me.lichris93.jrrp.jrrp.*;
import static me.lichris93.jrrp.thread.autoSummarizeYesterdayRank.yesterday_first;
import static me.lichris93.jrrp.values.*;
import static me.lichris93.jrrp.langs.*;

public class gameCommand implements TabExecutor {
    //Command
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, Command command, String s, String @NotNull [] strings) {
        if (strings.length == 0) {//'/jrrp'
            valueGenerate(commandSender);
            return true;
        }

        switch (strings[0].toLowerCase()) {
            case "get":
                get(commandSender, strings);
                break;
            case "clear":
                clear(commandSender, strings);
                break;
            case "reload":
                reload(commandSender, strings);
                break;
            case "rank":
                sendRank(commandSender, strings);
                break;
            case "save":
                save(commandSender, strings);
                break;
            case "monitor":
                monitor(commandSender, strings);
                break;
            case "start":
                threadStart(commandSender, strings);
                break;
            case "stop":
                threadStop(commandSender, strings);
                break;
            case "getaward":
                getAward(commandSender, strings);
                break;
            default:
                showHelp(commandSender);
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, Command command, String s, String @NotNull [] strings) {
        List<String> tabList = new ArrayList<>();
        if (strings.length == 1) {// /jrrp 这里必须比onCommand多1,我不知道为什么
            tabList.add("help");
            if (commandSender.hasPermission("jrrp.essential")) tabList.add("rank");
            if (award_enabled && commandSender.hasPermission("jrrp.essential")) tabList.add("getaward");
            if (commandSender.hasPermission("jrrp.clear")) tabList.add("clear");
            if (commandSender.hasPermission("jrrp.get")) tabList.add("get");
            if (commandSender.hasPermission("jrrp.reload")) tabList.add("reload");
            if (commandSender.hasPermission("jrrp.save")) tabList.add("save");
            if (commandSender.hasPermission("jrrp.monitor")) tabList.add("monitor");
            if (commandSender.hasPermission("jrrp.start")) tabList.add("start");
            if (commandSender.hasPermission("jrrp.stop")) tabList.add("stop");
            return tabList;
        }

        if (strings.length != 2) return Collections.emptyList();
        switch (strings[0].toLowerCase()) {
            case "clear":// /jrrp clear [name]
            case "get":// /jrrp get <name>
                for (Map.Entry<String, String[]> entry : DataMap.entrySet()) {
                    tabList.add(entry.getKey());
                }
                return tabList;
            case "stop":// /jrrp stop <Thread>
                if (autoSave_running) tabList.add("autoSave");
                if (autoSum_running) tabList.add("autoSum");
                if (autoGC_running) tabList.add("autoGC");
                if (autoRank_running) tabList.add("autoRank");
                return tabList;
            case "start":// /jrrp start <thread>
                if (!autoSave_running) tabList.add("autoSave");
                if (!autoSum_running) tabList.add("autoSum");
                if (!autoGC_running) tabList.add("autoGC");
                if (!autoRank_running) tabList.add("autoRank");
                return tabList;
        }
        return Collections.emptyList();
    }

    public void showHelp(@NotNull CommandSender commandSender) {
        commandSender.sendMessage("§a--------------[ jrrp ]--------------");
        commandSender.sendMessage(help_option);
        if (commandSender.hasPermission("jrrp.essential")) commandSender.sendMessage(help_jrrp);
        commandSender.sendMessage(help_jrrp_help);
        if (commandSender.hasPermission("jrrp.essential")) commandSender.sendMessage(help_jrrp_rank);
        if (award_enabled && commandSender.hasPermission("jrrp.essential"))
            commandSender.sendMessage(help_jrrp_getaward);
        if (commandSender.hasPermission("jrrp.clear")) commandSender.sendMessage(help_jrrp_clear);
        if (commandSender.hasPermission("jrrp.get")) commandSender.sendMessage(help_jrrp_get);
        if (commandSender.hasPermission("jrrp.reload")) commandSender.sendMessage(help_jrrp_reload);
        if (commandSender.hasPermission("jrrp.save")) commandSender.sendMessage(help_jrrp_save);
        if (commandSender.hasPermission("jrrp.monitor")) commandSender.sendMessage(help_jrrp_monitor);
        if (commandSender.hasPermission("jrrp.start")) commandSender.sendMessage(help_jrrp_start);
        if (commandSender.hasPermission("jrrp.stop")) commandSender.sendMessage(help_jrrp_stop);
        commandSender.sendMessage("§a----------[ By LiChris93 ]------------");
    }

    public void valueGenerate(@NotNull CommandSender commandSender) {
        if (unPass(0, "jrrp.essential", commandSender, new String[]{})) return;
        SimpleDateFormat f = new SimpleDateFormat("MM/dd");//格式化
        String date = f.format(new Date());//日期
        String name = commandSender.getName();//使用者名字
        if (!DataMap.containsKey(name)) {//如果无记录,则生成
            DataMap.put(name, new String[]{rand(), date});
            commandSender.sendMessage(generate_success.replace("{num}", DataMap.get(name)[0]));
        } else if (DataMap.get(name)[1].equals(date)) {//有记录且时间为当天，不生成
            commandSender.sendMessage(generate_duplicate.replace("{num}", DataMap.get(name)[0]));
        } else {//有记录但不是当天，生成
            DataMap.replace(name, new String[]{rand(), date});
            commandSender.sendMessage(generate_success.replace("{num}", DataMap.get(name)[0]));
        }
    }

    public void get(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(2, "jrrp.get", commandSender, strings)) return;

        if (DataMap.get(strings[1]) != null) {//成功获取
            commandSender.sendMessage(
                    get_num_success.replace("{player_name}", strings[1])
                            .replace("{num}", DataMap.get(strings[1])[0])
                            .replace("{date}", DataMap.get(strings[1])[1])
            );
        } else {//未能获取
            commandSender.sendMessage(get_num_fail.replace("{player_name}", strings[1]));
        }
    }

    public void clear(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass_clear(commandSender, strings)) return;

        // /jrrp clear (全部清除)
        if (strings.length == 1) {
            DataMap.clear();
            commandSender.sendMessage(clear_all_success);
            return;
        }
        //具体清除
        // /jrrp clear [name]
        if (DataMap.remove(strings[1]) != null) {//成功清空
            commandSender.sendMessage(clear_specific_player_success.replace("{player_name}", strings[1]));
        } else {//未能清空
            commandSender.sendMessage(clear_specific_player_fail.replace("{player_name}", strings[1]));
        }
    }

    public void reload(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(1, "jrrp.reload", commandSender, strings)) return;
        try {
            plugin.reloadConfig();
            config = plugin.getConfig();
            plugin.reloadAndGetLang();
            plugin.reloadAndGetData();
            loadConfig();
            loadLang();
            loadData();
            commandSender.sendMessage(reloaded_success);
        } catch (Exception e) {
            commandSender.sendMessage(reloaded_fail);
            e.printStackTrace();
        }
    }

    public void sendRank(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(1, "jrrp.essential", commandSender, strings)) return;

        autoRank.updateRank();
        int rank = 0;
        commandSender.sendMessage(rank_title);
        for (Map.Entry<String, Integer> entry : RankedMap.entrySet()) {
            rank += 1;
            commandSender.sendMessage(
                    rank_format.replace("{rank}", Integer.toString(rank))
                            .replace("{player_name}", entry.getKey())
                            .replace("{num}", Integer.toString(entry.getValue()))
            );
        }
    }

    public void save(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(1, "jrrp.save", commandSender, strings)) return;

        try {
            plugin.saveData();
            commandSender.sendMessage(save_success);
        } catch (Exception e) {
            commandSender.sendMessage(save_fail);
            e.printStackTrace();
        }

    }

    public void monitor(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(1, "jrrp.monitor", commandSender, strings)) return;

        commandSender.sendMessage(monitor_title);
        commandSender.sendMessage("§aautoRank:" + autoRank_running);
        commandSender.sendMessage("§aautoSum:" + autoSum_running);
        commandSender.sendMessage("§aautoSave:" + autoSave_running);
        commandSender.sendMessage("§aautoGC:" + autoGC_running);
    }

    public void threadStart(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(2, "jrrp.start", commandSender, strings)) return;
        switch (strings[1]) {
            case "autoGC":
                startAutoGC(commandSender);
                break;
            case "autoRank":
                startAutoRank(commandSender);
                break;
            case "autoSave":
                startAutoSave(commandSender);
                break;
            case "autoSum":
                startAutoSum(commandSender);
                break;
            default:
                commandSender.sendMessage(thread_not_exist);
                break;
        }
    }

    public void threadStop(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(2, "jrrp.stop", commandSender, strings)) return;
        switch (strings[1]) {
            case "autoGC":
                stopAutoGC(commandSender);
                break;
            case "autoRank":
                stopAutoRank(commandSender);
                break;
            case "autoSave":
                stopAutoSave(commandSender);
                break;
            case "autoSum":
                stopAutoSum(commandSender);
                break;
            default:
                commandSender.sendMessage(thread_not_exist);
                break;
        }
    }

    public void getAward(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(1, "jrrp.essential", commandSender, strings)) return;
        //没有启用
        if (!award_enabled) {
            commandSender.sendMessage(award_disabled);
            return;
        }
        //人不对
        if (!commandSender.getName().equals(yesterday_first[0])) {
            commandSender.sendMessage(not_yesterday_first);
            return;
        }
        //已被领完
        if (!award_available) {
            commandSender.sendMessage(already_awarded);
            return;
        }
        executeAwardCommand(commandSender.getServer().getPlayer(commandSender.getName()));
    }

    //utils
    public String rand() {
        return Integer.toString(new Random().nextInt(101));
    }

    public boolean unPass(int argNum, String Permission, @NotNull CommandSender commandSender, String @NotNull [] strings) {
        //仅适用于参数个数只有一种情况的时候 不止一种情况的话要单独写方法

        //没权限 显示帮助 不予通过
        if (!commandSender.hasPermission(Permission)) {
            showHelp(commandSender);
            return true;
        }
        //格式不对 显示帮助 不予通过
        if (strings.length != argNum) {
            showHelp(commandSender);
            return true;
        }
        return false;
    }

    public boolean unPass_clear(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        //没权限 显示帮助 不予通过
        if (!commandSender.hasPermission("jrrp.clear")) {
            showHelp(commandSender);
            return true;
        }
        //格式不对 显示帮助 不予通过
        if (strings.length < 1 || strings.length > 2) {
            showHelp(commandSender);
            return true;
        }
        return false;
    }

    public void startAutoGC(@NotNull CommandSender commandSender) {
        if (autoGC_running) {//线程已在运行
            commandSender.sendMessage(thread_already_running.replace("{name}", "autoGC"));
            return;
        }
        autoGC_thread = new autoGC();//重新创建对象
        try {//异常处理
            autoGC_thread.start();
        } catch (Exception e) {
            commandSender.sendMessage(thread_start_fail.replace("{name}", "autoGC"));
            e.printStackTrace();
            return;
        }
        commandSender.sendMessage(thread_start_success.replace("{name}", "autoGC"));
    }

    public void stopAutoGC(@NotNull CommandSender commandSender) {
        if (!autoGC_running) {//线程已被停止
            commandSender.sendMessage(thread_already_stopped.replace("{name}", "autoGC"));
            return;
        }
        try {//异常处理
            autoGC_thread.interrupt();
        } catch (Exception e) {
            commandSender.sendMessage(thread_stop_fail.replace("{name}", "autoGC"));
            e.printStackTrace();
            return;
        }
        commandSender.sendMessage(thread_stop_success.replace("{name}", "autoGC"));
    }

    public void startAutoRank(@NotNull CommandSender commandSender) {
        if (autoRank_running) {//线程已在运行
            commandSender.sendMessage(thread_already_running.replace("{name}", "autoRank"));
            return;
        }
        autoRank_thread = new autoRank();//重新创建对象
        try {//异常处理
            autoRank_thread.start();
        } catch (Exception e) {
            commandSender.sendMessage(thread_start_fail.replace("{name}", "autoRank"));
            e.printStackTrace();
            return;
        }
        commandSender.sendMessage(thread_start_success.replace("{name}", "autoRank"));
    }

    public void stopAutoRank(@NotNull CommandSender commandSender) {
        if (!autoRank_running) {//线程已被停止
            commandSender.sendMessage(thread_already_stopped.replace("{name}", "autoRank"));
            return;
        }
        try {//异常处理
            autoRank_thread.interrupt();
        } catch (Exception e) {
            commandSender.sendMessage(thread_stop_fail.replace("{name}", "autoRank"));
            e.printStackTrace();
            return;
        }
        commandSender.sendMessage(thread_stop_success.replace("{name}", "autoRank"));
    }

    public void startAutoSave(@NotNull CommandSender commandSender) {
        if (autoSave_running) {//线程已在运行
            commandSender.sendMessage(thread_already_running.replace("{name}", "autoSave"));
            return;
        }
        autoSave_thread = new autoSave();//重新创建对象
        try {//异常处理
            autoSave_thread.start();
        } catch (Exception e) {
            commandSender.sendMessage(thread_start_fail.replace("{name}", "autoSave"));
            e.printStackTrace();
            return;
        }
        commandSender.sendMessage(thread_start_success.replace("{name}", "autoSave"));
    }

    public void stopAutoSave(@NotNull CommandSender commandSender) {
        if (!autoSave_running) {//线程已被停止
            commandSender.sendMessage(thread_already_stopped.replace("{name}", "autoSave"));
            return;
        }
        try {//异常处理
            autoSave_thread.interrupt();
        } catch (Exception e) {
            commandSender.sendMessage(thread_stop_fail.replace("{name}", "autoSave"));
            e.printStackTrace();
            return;
        }
        commandSender.sendMessage(thread_stop_success.replace("{name}", "autoSave"));
    }

    public void startAutoSum(@NotNull CommandSender commandSender) {
        if (autoSum_running) {//线程已在运行
            commandSender.sendMessage(thread_already_running.replace("{name}", "autoSum"));
            return;
        }
        autoSum_thread = new autoSummarizeYesterdayRank();//重新创建对象
        try {//异常处理
            autoSum_thread.start();
        } catch (Exception e) {
            commandSender.sendMessage(thread_start_fail.replace("{name}", "autoSum"));
            e.printStackTrace();
            return;
        }
        commandSender.sendMessage(thread_start_success.replace("{name}", "autoSum"));
    }

    public void stopAutoSum(@NotNull CommandSender commandSender) {
        if (!autoSum_running) {//线程已被停止
            commandSender.sendMessage(thread_already_stopped.replace("{name}", "autoSum"));
            return;
        }
        try {//异常处理
            autoSum_thread.interrupt();
        } catch (Exception e) {
            commandSender.sendMessage(thread_stop_fail.replace("{name}", "autoSum"));
            e.printStackTrace();
            return;
        }
        commandSender.sendMessage(thread_stop_success.replace("{name}", "autoSum"));
    }

}

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

import static me.lichris93.jrrp.jrrp.loadConfig;
import static me.lichris93.jrrp.jrrp.loadData;
import static me.lichris93.jrrp.values.*;
import static me.lichris93.jrrp.langs.*;

public class gameCommand implements TabExecutor {
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
            default:
                showHelp(commandSender);
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, Command command, String s, String @NotNull [] strings) {
        List<String> tabList = new ArrayList<>();
        boolean isOP = commandSender.isOp();
        if (strings.length == 1) {// /jrrp 这里必须比onCommand多1,我不知道为什么
            tabList.add("help");
            tabList.add("rank");
            if (isOP) {
                tabList.add("clear");
                tabList.add("get");
                tabList.add("reload");
                tabList.add("save");
                tabList.add("monitor");
                tabList.add("start");
                tabList.add("stop");
            }
            return tabList;
        }

        if (!isOP || strings.length != 2) return Collections.emptyList();
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
        commandSender.sendMessage(help_jrrp);
        commandSender.sendMessage(help_jrrp_help);
        commandSender.sendMessage(help_jrrp_rank);
        if (commandSender.isOp()) {
            commandSender.sendMessage(help_jrrp_clear);
            commandSender.sendMessage(help_jrrp_get);
            commandSender.sendMessage(help_jrrp_reload);
            commandSender.sendMessage(help_jrrp_save);
            commandSender.sendMessage(help_jrrp_monitor);
        }
        commandSender.sendMessage("§a----------[ By LiChris93 ]------------");
    }

    public void valueGenerate(@NotNull CommandSender commandSender) {
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
        if (unPass(2, true, commandSender, strings)) return;

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
        if (unPass(1, true, commandSender, strings)) return;

        try {
            plugin.reloadConfig();
            config = plugin.getConfig();
            plugin.reloadAndGetLang();
            plugin.reloadAndGetData();
            loadData();
            loadConfig();
            commandSender.sendMessage(reloaded_success);
        } catch (Exception e) {
            commandSender.sendMessage(reloaded_fail);
            e.printStackTrace();
        }
    }

    public void sendRank(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(1, false, commandSender, strings)) return;

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
        if (unPass(1, true, commandSender, strings)) return;

        try {
            plugin.saveData();
            commandSender.sendMessage(save_success);
        } catch (Exception e) {
            commandSender.sendMessage(save_fail);
            e.printStackTrace();
        }

    }

    public void monitor(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(1, true, commandSender, strings)) return;

        commandSender.sendMessage(monitor_title);
        commandSender.sendMessage("§aautoRank:" + autoRank_running);
        commandSender.sendMessage("§aautoSum:" + autoSum_running);
        commandSender.sendMessage("§aautoSave:" + autoSave_running);
        commandSender.sendMessage("§aautoGC:" + autoGC_running);
    }

    public void threadStart(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(2, true, commandSender, strings)) return;
        switch (strings[1]) {
            case "autoGC":
                if (autoGC_running) {//线程已在运行
                    commandSender.sendMessage(thread_already_running.replace("{name}", strings[1]));
                    return;
                }
                autoGC_thread = new autoGC();//重新创建对象
                try {//异常处理
                    autoGC_thread.start();
                } catch (Exception e) {
                    commandSender.sendMessage(thread_start_fail.replace("{name}", strings[1]));
                    e.printStackTrace();
                    return;
                }
                autoGC_running = true;
                commandSender.sendMessage(thread_start_success.replace("{name}", strings[1]));
                break;
            case "autoRank":
                if (autoRank_running) {//线程已在运行
                    commandSender.sendMessage(thread_already_running.replace("{name}", strings[1]));
                    return;
                }
                autoRank_thread = new autoRank();//重新创建对象
                try {//异常处理
                    autoRank_thread.start();
                } catch (Exception e) {
                    commandSender.sendMessage(thread_start_fail.replace("{name}", strings[1]));
                    e.printStackTrace();
                    return;
                }
                autoRank_running = true;
                commandSender.sendMessage(thread_start_success.replace("{name}", strings[1]));
                break;
            case "autoSave":
                if (autoSave_running) {//线程已在运行
                    commandSender.sendMessage(thread_already_running.replace("{name}", strings[1]));
                    return;
                }
                autoSave_thread = new autoSave();//重新创建对象
                try {//异常处理
                    autoSave_thread.start();
                } catch (Exception e) {
                    commandSender.sendMessage(thread_start_fail.replace("{name}", strings[1]));
                    e.printStackTrace();
                    return;
                }
                autoSave_running = true;
                commandSender.sendMessage(thread_start_success.replace("{name}", strings[1]));
                break;
            case "autoSum":
                if (autoSum_running) {//线程已在运行
                    commandSender.sendMessage(thread_already_running.replace("{name}", strings[1]));
                    return;
                }
                autoSum_thread = new autoSummarizeYesterdayRank();//重新创建对象
                try {//异常处理
                    autoSum_thread.start();
                } catch (Exception e) {
                    commandSender.sendMessage(thread_start_fail.replace("{name}", strings[1]));
                    e.printStackTrace();
                    return;
                }
                autoSum_running = true;
                commandSender.sendMessage(thread_start_success.replace("{name}", strings[1]));
                break;
            default:
                commandSender.sendMessage(thread_not_exist);
                break;
        }
    }

    public void threadStop(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (unPass(2, true, commandSender, strings)) return;
        switch (strings[1]) {
            case "autoGC":
                if (!autoGC_running) {//线程已被停止
                    commandSender.sendMessage(thread_already_stopped.replace("{name}", strings[1]));
                    return;
                }
                try {//异常处理
                    autoGC_thread.interrupt();
                } catch (Exception e) {
                    commandSender.sendMessage(thread_stop_fail.replace("{name}", strings[1]));
                    e.printStackTrace();
                    return;
                }
                autoGC_running = false;
                commandSender.sendMessage(thread_stop_success.replace("{name}", strings[1]));
                break;
            case "autoRank":
                if (!autoRank_running) {//线程已被停止
                    commandSender.sendMessage(thread_already_stopped.replace("{name}", strings[1]));
                    return;
                }
                try {//异常处理
                    autoRank_thread.interrupt();
                } catch (Exception e) {
                    commandSender.sendMessage(thread_stop_fail.replace("{name}", strings[1]));
                    e.printStackTrace();
                    return;
                }
                autoRank_running = false;
                commandSender.sendMessage(thread_stop_success.replace("{name}", strings[1]));
                break;
            case "autoSave":
                if (!autoSave_running) {//线程已被停止
                    commandSender.sendMessage(thread_already_stopped.replace("{name}", strings[1]));
                    return;
                }
                try {//异常处理
                    autoSave_thread.interrupt();
                } catch (Exception e) {
                    commandSender.sendMessage(thread_stop_fail.replace("{name}", strings[1]));
                    e.printStackTrace();
                    return;
                }
                autoSave_running = false;
                commandSender.sendMessage(thread_stop_success.replace("{name}", strings[1]));
                break;
            case "autoSum":
                if (!autoSum_running) {//线程已被停止
                    commandSender.sendMessage(thread_already_stopped.replace("{name}", strings[1]));
                    return;
                }
                try {//异常处理
                    autoSum_thread.interrupt();
                } catch (Exception e) {
                    commandSender.sendMessage(thread_stop_fail.replace("{name}", strings[1]));
                    e.printStackTrace();
                    return;
                }
                autoSum_running = false;
                commandSender.sendMessage(thread_stop_success.replace("{name}", strings[1]));
                break;
            default:
                commandSender.sendMessage(thread_not_exist);
                break;
        }
    }

    public String rand() {
        return Integer.toString(new Random().nextInt(101));
    }

    public boolean unPass(int argNum, boolean needOP, @NotNull CommandSender commandSender, String @NotNull [] strings) {
        //仅适用于参数个数只有一种情况的时候 不止一种情况的话要单独写方法

        //没权限 显示帮助 不予通过
        if (needOP && !commandSender.isOp()) {
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
        if (!commandSender.isOp()) {
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
}

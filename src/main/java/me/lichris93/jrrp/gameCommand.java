package me.lichris93.jrrp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

import static me.lichris93.jrrp.jrrp.loadConfig;
import static me.lichris93.jrrp.values.*;
import static me.lichris93.jrrp.langs.*;

public class gameCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, Command command, String s, String @NotNull [] strings) {
        boolean isOP = commandSender.isOp();
        if (strings.length == 0) {//'/jrrp'
            valueGenerate(commandSender);
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("clear") && isOP) {
            //'/jrrp clear'
            DataMap.clear();
            commandSender.sendMessage(clear_all_success);
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("clear") && isOP) {
            //'/jrrp clear [name]'
            clear_specific(commandSender, strings);
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("get") && isOP) {
            //'/jrrp get <name>'
            get(commandSender, strings);
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("reload") && isOP) {
            //'/jrrp reload
            reload(commandSender);
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("rank")) {
            //'/jrrp rank'
            sendRank(commandSender);
        } else {
            showHelp(commandSender);
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
            }
            return tabList;
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("clear") && isOP) {// /jrrp clear [name]
            for (Map.Entry<String, String[]> entry : DataMap.entrySet()) {
                tabList.add(entry.getKey());
            }
            return tabList;
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("get") && isOP) {// /jrrp get <name>
            for (Map.Entry<String, String[]> entry : DataMap.entrySet()) {
                tabList.add(entry.getKey());
            }
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

    public void clear_specific(@NotNull CommandSender commandSender, String @NotNull [] strings) {
        if (DataMap.remove(strings[1]) != null) {//成功清空
            commandSender.sendMessage(clear_specific_player_success.replace("{player_name}", strings[1]));
        } else {//未能清空
            commandSender.sendMessage(clear_specific_player_fail.replace("{player_name}", strings[1]));
        }
    }

    public void reload(@NotNull CommandSender commandSender) {
        plugin.reloadConfig();
        config = plugin.getConfig();
        plugin.reloadAndGetLang();
        loadConfig();
        commandSender.sendMessage(reloaded);
    }

    public void sendRank(@NotNull CommandSender commandSender) {
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

    public String rand() {
        return Integer.toString(new Random().nextInt(101));
    }
}

package me.lichris93.jrrp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static me.lichris93.jrrp.values.*;

public class gameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String @NotNull [] strings) {
        if (strings.length == 0) {//'/jrrp'
            valueGenerate(commandSender);
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("clear") && commandSender.isOp()) {
            //'/jrrp clear'
            DataMap.clear();
            commandSender.sendMessage("§a成功清除所有数据!");
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("clear") && commandSender.isOp()) {
            //'/jrrp clear [name]'
            if (DataMap.remove(strings[1]) != null) {
                commandSender.sendMessage("§a成功清除" + strings[1] + "的数据!");
            } else {
                commandSender.sendMessage("§a无法清除" + strings[1] + "的数据!");
            }
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("get") && commandSender.isOp()) {
            //'/jrrp get <name>'
            if (DataMap.get(strings[1]) != null) {
                commandSender.sendMessage("§a" + strings[1] + "的人品值为:" + DataMap.get(strings[1])[0] + ",于" + DataMap.get(strings[1])[1] + "取得");
            } else {
                commandSender.sendMessage("§a无法获得" + strings[1] + "的数据!");
            }
        } else {
            showHelp(commandSender);
        }
        return true;
    }

    public void showHelp(@NotNull CommandSender commandSender) {
        commandSender.sendMessage("§a--------------[ jrrp ]--------------");
        commandSender.sendMessage("§a[]为可选,<>为必填");
        commandSender.sendMessage("§a/jrrp                生成/查看今日人品值");
        commandSender.sendMessage("§a/jrrp help               显示本帮助信息");
        if (commandSender.isOp()) {
            commandSender.sendMessage("§a/jrrp clear [name]            清空数据");
            commandSender.sendMessage("§a/jrrp get <name>            获取指定人的值");
        }
        commandSender.sendMessage("§a----------[ By LiChris93 ]-----------");
    }

    public void valueGenerate(@NotNull CommandSender commandSender) {
        SimpleDateFormat f = new SimpleDateFormat("MM/dd");//格式化
        String date = f.format(new Date());//日期
        String name = commandSender.getName();//使用者名字
        if (!DataMap.containsKey(name)) {//如果无记录,则生成
            DataMap.put(name, new String[]{rand(), date});
            commandSender.sendMessage("§a生成成功!生成的值为:" + DataMap.get(name)[0]);
        } else if (DataMap.get(name)[1].equals(date)) {//有记录且时间为当天，不生成
            commandSender.sendMessage("§a您今天已经生成过了!上次生成的值为:" + DataMap.get(name)[0]);
        } else {//有记录但不是当天，生成
            DataMap.replace(name, new String[]{rand(), date});
            commandSender.sendMessage("§a生成成功!生成的值为:" + DataMap.get(name)[0]);
        }
    }

    public String rand() {
        return Integer.toString(new Random().nextInt(101));
    }
}

package me.lichris93.jrrp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import static me.lichris93.jrrp.langs.award_successfully;
import static me.lichris93.jrrp.thread.autoSummarizeYesterdayRank.yesterday_first;
import static me.lichris93.jrrp.values.*;

public class autoAwardWhenJoin implements Listener {
    @EventHandler
    public void onPlayerJoinServer(@NotNull PlayerJoinEvent event) {
        if (!award_enabled) return;//未启用奖励功能
        if (!auto_award_when_join_enabled) return;//未启用进服自动奖励功能
        if (!event.getPlayer().getName().equals(yesterday_first[0])) return;//人不对
        if (!award_available) return;//已领完
        executeAwardCommand(event.getPlayer());
    }

    public static void executeAwardCommand(@NotNull Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), award_command.replace("{name}", player.getName()));
        player.sendMessage(award_successfully);
        award_available = false;
    }
}

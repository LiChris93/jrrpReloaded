package me.lichris93.jrrp;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.lichris93.jrrp.values.*;

public class papi extends PlaceholderExpansion {
    public final jrrp plugin;

    public papi(jrrp plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "jrrp";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        try {
            if (params.equalsIgnoreCase("num")) {
                return DataMap.get(player.getName())[0]; //
            }
            if (params.equalsIgnoreCase("num_colored")) {
                String num = DataMap.get(player.getName())[0];
                if (Integer.parseInt(num) < 25) {
                    return "§4" + num;
                } else if (Integer.parseInt(num) < 50) {
                    return "§e" + num;
                } else if (Integer.parseInt(num) < 75) {
                    return "§b" + num;
                } else {
                    return "§a" + num;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}

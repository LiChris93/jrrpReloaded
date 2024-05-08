package me.lichris93.jrrp;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        try {
            switch (params.toLowerCase()) {
                case "num":
                    return DataMap.get(player.getName())[0];
                case "num_colored":
                    return colorNum(DataMap.get(player.getName())[0]);
                case "first_player":
                    return getNameByRank(1);
                case "second_player":
                    return getNameByRank(2);
                case "third_player":
                    return getNameByRank(3);
                case "first_num":
                    return Integer.toString(getValueByRank(1));
                case "second_num":
                    return Integer.toString(getValueByRank(2));
                case "third_num":
                    return Integer.toString(getValueByRank(3));
                case "first_num_colored":
                    return colorNum(Integer.toString(getValueByRank(1)));
                case "second_num_colored":
                    return colorNum(Integer.toString(getValueByRank(2)));
                case "third_num_colored":
                    return colorNum(Integer.toString(getValueByRank(3)));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public String getNameByRank(int rank) {
        int position = 0;
        for (Map.Entry<String, Integer> entry : RankedMap.entrySet()) {
            position += 1;
            if (position == rank) {
                return entry.getKey();
            }
        }
        return "";
    }

    public Integer getValueByRank(int rank) {
        int position = 0;
        for (Map.Entry<String, Integer> entry : RankedMap.entrySet()) {
            position += 1;
            if (position == rank) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public String colorNum(String num) {
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
}

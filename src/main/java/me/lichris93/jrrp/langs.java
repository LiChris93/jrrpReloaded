package me.lichris93.jrrp;

public class langs {
    static String clear_all_success = "§aSuccessfully cleared all data!";
    static String clear_specific_player_success = "§aSuccessfully cleared data of {player_name}!";
    static String clear_specific_player_fail = "§aFailed to clear data of {player_name}!";
    static String get_num_success = "§a{player_name}'s luck rate is {num},got on {date}.";
    static String get_num_fail = "§aUnable to get data of {player_name}.";
    static String generate_success = "§aSuccessfully generated!Your luck rate is {num}.";
    static String generate_duplicate = "§aYou have already generated your luck rate today!The luck rate you generated was {num}.";
    static String rank_title = "§a--------------  Rank List  --------------";
    static String rank_format = "§a{rank}.{player_name}:{num}";
    static String gc_success = "§a[jrrp-GC]Successfully removed {removed_count} expired data on {date}!";
    static String reloaded = "§aSuccessfully reloaded config!";
    static String yesterday_summarized = "§a§l{date}'s rank has been summarized!";

    static String finish_enable = "jrrp load has finished!——By LiChris93[{millis}ms]";
    static String on_disable = "jrrp disabled successfully!——By LiChris93";
    static String reg_command_success = "Successfully registered command executor.";
    static String reg_command_fail = "Failed to register command executor.";
    static String read_config_success = "Successfully read config.";
    static String read_config_fail = "Failed to read config.";
    static String papi_success = "Successfully registered PAPI.";
    static String no_papi = "PAPI can't be found,skip.";
    static String papi_fail = "Failed to register PAPI.";
    static String gc_start_success = "GC Thread has started successfully.";
    static String gc_start_fail = "Failed to start GC Thread.";
    static String rank_start_success = "Auto ranking Thread has started successfully.";
    static String rank_start_fail = "Failed to start Auto ranking Thread.";

    static String help_option = "§a[] is optional,<> is necessary.";
    static String help_jrrp = "§a/jrrp            generate/see your luck rate";
    static String help_jrrp_help = "§a/jrrp help               show this help message";
    static String help_jrrp_rank = "§a/jrrp rank                 show the rank list";
    static String help_jrrp_clear = "§a/jrrp clear [name]                 clear data";
    static String help_jrrp_get = "§a/jrrp get <name>     get somebody's luck rate";
    static String help_jrrp_reload = "§a/jrrp reload                reload the config";
}

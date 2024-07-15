package me.lichris93.jrrp;

public class langs {
    public static String clear_all_success = "§aSuccessfully cleared all data!";
    public static String clear_specific_player_success = "§aSuccessfully cleared data of {player_name}!";
    public static String clear_specific_player_fail = "§cFailed to clear data of {player_name}!";
    public static String get_num_success = "§a{player_name}'s luck rate is {num},got on {date}.";
    public static String get_num_fail = "§cUnable to get data of {player_name}.";
    public static String generate_success = "§aSuccessfully generated!Your luck rate is {num}.";
    public static String generate_duplicate = "§cYou have already generated your luck rate today!The luck rate you generated was {num}.";
    public static String rank_title = "§a--------------  Rank List  --------------";
    public static String rank_format = "§a{rank}.{player_name}:{num}";
    public static String gc_success = "§a[jrrp-GC]Successfully removed {removed_count} expired data on {date}!";
    public static String reloaded_success = "§aSuccessfully reloaded config!";
    public static String reloaded_fail = "§cFailed to reload config! You can know the detail in the console.";
    public static String yesterday_summarized = "§a§l{date}'s rank has been summarized!";
    public static String monitor_title = "§aRunning status of each thread:";
    public static String save_success = "§aSuccessfully saved!";
    public static String save_fail = "§cFailed to Save! You can know the detail in the console!";

    public static String finish_enable = "jrrp load has finished!——By LiChris93[{millis}ms]";
    public static String on_disable = "jrrp disabled successfully!——By LiChris93";
    public static String reg_command_success = "Successfully registered command executor.";
    public static String reg_command_fail = "Failed to register command executor.";
    public static String read_config_success = "Successfully read config.";
    public static String read_config_fail = "Failed to read config.";
    public static String papi_success = "Successfully registered PAPI.";
    public static String no_papi = "PAPI can't be found,skip.";
    public static String papi_fail = "Failed to register PAPI.";
    public static String gc_start_success = "GC Thread has started successfully.";
    public static String gc_start_fail = "Failed to start GC Thread.";
    public static String rank_start_success = "Auto ranking Thread has started successfully.";
    public static String rank_start_fail = "Failed to start Auto ranking Thread.";
    public static String sum_start_success = "Auto summarizing Thread has started successfully.";
    public static String sum_start_fail = "Failed to start Auto summarizing Thread.";
    public static String save_start_success = "Auto saving Thread has started successfully.";
    public static String save_start_fail = "Failed to start Auto saving Thread.";
    public static String save_disabled = "Auto saving Thread is disabled.";

    public static String data_read_success = "Successfully read data from file.";
    public static String data_read_fail = "Failed to read data from file.";
    public static String data_expired = "The data in the file has expired.Ignore and delete.";

    public static String autoGC_stopped = "The autoGC thread has stopped.";
    public static String autoRank_stopped = "The autoRank thread has stopped.";
    public static String autoSave_stopped = "The autoSave thread has stopped.";
    public static String autoSum_stopped = "The autoSum thread has stopped.";

    public static String thread_already_running = "The thread {name} is already running!";
    public static String thread_already_stopped = "The thread {name} is already stopped!";
    public static String thread_start_success = "Thread {name} has started successfully!";
    public static String thread_start_fail = "Failed to start thread {name}!";
    public static String thread_stop_success = "Thread {name} has stopped successfully!";
    public static String thread_stop_fail = "Failed to stop thread {name}!";
    public static String thread_not_exist = "The thread doesn't exist!";

    public static String help_option = "§a[] is optional,<> is necessary.";
    public static String help_jrrp = "§a/jrrp            generate/see your luck rate";
    public static String help_jrrp_help = "§a/jrrp help               show this help message";
    public static String help_jrrp_rank = "§a/jrrp rank                 show the rank list";
    public static String help_jrrp_clear = "§a/jrrp clear [name]                 clear data";
    public static String help_jrrp_get = "§a/jrrp get <name>     get somebody's luck rate";
    public static String help_jrrp_reload = "§a/jrrp reload                reload the config";
    public static String help_jrrp_save = "§a/jrrp save                Save the data";
    public static String help_jrrp_monitor = "§a/jrrp monitor      Monitor the status of each thread";
}

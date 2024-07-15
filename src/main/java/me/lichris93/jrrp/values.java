package me.lichris93.jrrp;

import me.lichris93.jrrp.thread.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;

public class values {
    public static HashMap<String, String[]> DataMap = new HashMap<>();//[name,[value,date]]
    public static HashMap<String, Integer> RankedMap = new HashMap<>();//[name,value]
    public static jrrp plugin;
    public static FileConfiguration config;
    public static FileConfiguration langsYML;
    public static File langsFile;
    public static FileConfiguration dataYML;
    public static File dataFile;

    public static boolean autosave_enabled;
    public static int autosave_interval;

    public static boolean autoSave_running = false;
    public static boolean autoRank_running = false;
    public static boolean autoSum_running = false;
    public static boolean autoGC_running = false;

    public static autoRank autoRank_thread;
    public static autoGC autoGC_thread;
    public static autoSave autoSave_thread;
    public static autoSummarizeYesterdayRank autoSum_thread;
}

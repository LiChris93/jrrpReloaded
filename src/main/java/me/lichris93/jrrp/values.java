package me.lichris93.jrrp;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;

public class values {
    static HashMap<String, String[]> DataMap = new HashMap<>();//[name,[value,date]]
    static HashMap<String, Integer> RankedMap = new HashMap<>();//[name,value]
    static jrrp plugin;
    static FileConfiguration config;
    static FileConfiguration langsYML;
    static File langsFile;
}

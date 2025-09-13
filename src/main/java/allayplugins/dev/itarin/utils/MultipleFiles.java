package allayplugins.dev.itarin.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MultipleFiles {

    public static void createFolder(JavaPlugin plugin, String folder) {
        try {
            File paste = new File(plugin.getDataFolder(), folder);

            if (!paste.exists())
                paste.mkdirs();

        } catch (Throwable e) {
            Bukkit.getConsoleSender().sendMessage("§cNão foi possível criar a pasta §6" + folder + "§c.");
            e.printStackTrace();
        }
    }

    public static void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§cNão foi possível criar o arquivo §6" + file.getName() + "§c.");
            e.printStackTrace();
        }
    }

    public static File getFolder(JavaPlugin plugin, String folder) {
        return new File(plugin.getDataFolder(), folder);
    }

    public static File getFile(JavaPlugin plugin, String fileName) {
        return new File(plugin.getDataFolder(), fileName + ".yml");
    }

    public static File getFile(JavaPlugin plugin, String folder, String fileName) {
        return new File(new File(plugin.getDataFolder(), folder), fileName + ".yml");
    }

    public static FileConfiguration getConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void deleteFile(File file) {
        file.delete();
    }

    public static void createConfig(JavaPlugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName + ".yml");
        if (!file.exists()) {
            plugin.saveResource(fileName + ".yml", false);
        }
    }

    public static FileConfiguration getConfig(JavaPlugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void createConfig(JavaPlugin plugin, String folder, String fileName) {
        File file = new File(plugin.getDataFolder(), folder + "/" + fileName + ".yml");
        if (!file.exists()) {
            plugin.saveResource(folder + "/" + fileName + ".yml", false);
        }
    }

    public static FileConfiguration getConfig(JavaPlugin plugin, String folder, String fileName) {
        File file = new File(plugin.getDataFolder(), folder + "/" + fileName + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

}

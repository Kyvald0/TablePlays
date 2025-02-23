package org.pythonchik.tableplays;

import org.pythonchik.tableplays.managers.RecipeManager;
import org.pythonchik.tableplays.managers.translationManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public final class TablePlays extends JavaPlugin implements Listener {

    public static Plugin instance;
    public static FileConfiguration config = null;
    private static FileConfiguration translations = null;
    public static FileConfiguration data = null;
    public static boolean isDevAndIsMiniking1000TheBestPlayerInHisMind = true;
    public static Plugin getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        new translationManager(translations);
        Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
        RecipeManager.init(this);
        if (isDevAndIsMiniking1000TheBestPlayerInHisMind) {
            getCommand("gimme").setExecutor(new commands());
            getCommand("base64").setExecutor(new commands());
        }
    }

    @Override
    public void onDisable() {
        try {
            data.save(new File(getDataFolder(), "saved.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void loadConfig() {
        if (isDevAndIsMiniking1000TheBestPlayerInHisMind) {
            saveResource("config.yml", true);
            File configFile = new File(getDataFolder(), "config.yml");
            config = YamlConfiguration.loadConfiguration(configFile);
            saveResource("ru.yml", true);
            File translFile = new File(getDataFolder(),  "ru.yml");
            translations = YamlConfiguration.loadConfiguration(translFile);
            saveResource("saved.yml", false);
            File dataa = new File(getDataFolder(), "saved.yml");
            data = YamlConfiguration.loadConfiguration(dataa);
            return;
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        if (config.getString("lang") != null) {
            File translFile = new File(getDataFolder(),  config.getString("lang") + ".yml");
            if (!translFile.exists()) {
                String[] names = new String[]{"ru", "en", "what"};
                if (Arrays.stream(names).toList().contains(config.getString("lang"))) saveResource(config.getString("lang") + ".yml", false);
            }
            if (translFile.exists()) {
                translations = YamlConfiguration.loadConfiguration(translFile);
            } else {
                File backup = new File(getDataFolder(), "ru.yml");
                saveResource("ru.yml", false);
                translations = YamlConfiguration.loadConfiguration(backup);
            }

        }
    }

}

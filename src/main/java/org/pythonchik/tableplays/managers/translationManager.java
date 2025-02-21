package org.pythonchik.tableplays.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class translationManager {

    public static translationManager instance = null;
    private static FileConfiguration translations = null;
    public static translationManager getInstance() {
        return instance;
    }

    public translationManager(FileConfiguration trans) {
        instance = this;
        translations = trans;
    }

    public String getName(String type) {
        return ChatColor.translateAlternateColorCodes('&', translations.getString(type + ".name", "Автор перевода лох."));
    }

    public List<String> getLore(String type) {
        List<String> lore = translations.getStringList(type + ".lore");
        lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
        return lore;
    }
}

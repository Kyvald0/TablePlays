package org.pythonchik.tableplays.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.C;

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
        return ChatColor.translateAlternateColorCodes('&', translations.getString(type + ".name", "Автор перевода лох.")) ;
    }

    public String getName(ItemStack item) {
        String type = item.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING);
        try {
            String sub = "_" + item.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER);
            return ChatColor.translateAlternateColorCodes('&', translations.getString(type + sub + ".name"));
        } catch (Exception ignored) {
            return ChatColor.translateAlternateColorCodes('&', translations.getString(type + ".name", "Автор перевода лох."));
        }
    }

    public List<String> getLore(String type) {
        List<String> lore = translations.getStringList(type + ".lore");
        lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
        return lore;
    }

    public List<String> getLore(ItemStack item) {
        //TODO add sub-type lore
        String type = item.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING);
        List<String> lore = translations.getStringList(type + ".lore");
        lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
        lore.replaceAll(textToTranslate -> {
            String translated = ChatColor.translateAlternateColorCodes('&', textToTranslate);
            // Bundle Number (of) ITEMS
            translated = translated.replace("%BNITEMS%", String.valueOf(BundleManager.getBundleItems(item).size()));

            // Bundle Max Items
            translated = translated.replace("%BMI%", String.valueOf(BundleManager.getMaxItems(item)));

            return translated;
        });

        return lore;
    }



}

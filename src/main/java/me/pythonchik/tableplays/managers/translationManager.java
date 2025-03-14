package me.pythonchik.tableplays.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
    // they may be unused now, but I can see, how there might be a situation in the future, where it might be usefull
    public String getName(String type) {
        return ChatColor.translateAlternateColorCodes('&', translations.getString(type + ".name", "Автор перевода лох.")) ;
    }

    public List<String> getLore(String type) {
        List<String> lore = translations.getStringList(type + ".lore");
        lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
        return lore;
    }

    public String getName(ItemStack item) {
        String type = item.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING);
        if (item.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER) != null) {
            String sub = ".s" + item.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER);
            if (translations.contains(type + sub + ".name")) {
                return ChatColor.translateAlternateColorCodes('&', translations.getString(type + sub + ".name", "&4&lАвтор перевода лох."));
            }
        }
        return ChatColor.translateAlternateColorCodes('&', translations.getString(type + ".name", "&4&lАвтор перевода лох."));
    }


    public List<String> getLore(ItemStack item) {
        String type = item.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.Item.getValue(), PersistentDataType.STRING);
        List<String> lore;
        try {
            String sub = ".s" + item.getItemMeta().getPersistentDataContainer().get(Util.ItemTags.SubType.getValue(), PersistentDataType.INTEGER);
            lore = translations.getStringList(type + sub + ".lore");
            if (lore.equals(List.of())) throw new Exception(); // if lore is empty -> load it from default, so just throw an empty exception and go to catch
        } catch (Exception ignored) {
            lore = translations.getStringList(type + ".lore");
        }
        lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
        lore.replaceAll(textToTranslate -> {
            String translated = ChatColor.translateAlternateColorCodes('&', textToTranslate);
            // Bundle Number (of) ITEMS
            translated = translated.replace("%BNITEMS%", String.valueOf(BundleManager.getBundleCurrentItems(item)));

            // Bundle Max Items
            translated = translated.replace("%BMI%", String.valueOf(BundleManager.getMaxItems(item)));

            return translated;
        });

        return lore;
    }



}

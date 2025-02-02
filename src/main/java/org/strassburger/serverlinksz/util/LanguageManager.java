package org.strassburger.serverlinksz.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.strassburger.serverlinksz.ServerLinksZ;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class LanguageManager {
    private final JavaPlugin plugin = ServerLinksZ.getInstance();
    public static final List<String> defaultLangs = List.of("en-US", "de-DE", "zh-CN", "ru-RU", "zh-TW");

    private HashMap<String, String> translationMap;
    private FileConfiguration langConfig;

    public LanguageManager() {
        loadLanguageConfig();
    }

    /**
     * Reloads the language configuration
     */
    public void reload() {
        loadLanguageConfig();
    }

    /**
     * Loads the language configuration
     */
    private void loadLanguageConfig() {
        File languageDirectory = new File(plugin.getDataFolder(), "lang/");
        if (!languageDirectory.exists() || !languageDirectory.isDirectory()) languageDirectory.mkdir();

        for (String langString : defaultLangs) {
            File langFile = new File("lang/", langString + ".yml");
            if (!new File(languageDirectory, langString + ".yml").exists()) {
                plugin.getLogger().info("Saving file " + langFile.getPath());
                plugin.saveResource(langFile.getPath(), false);
            }
        }

        String langOption = plugin.getConfig().getString("lang") != null ? plugin.getConfig().getString("lang") : "en-US";
        File selectedLangFile = new File(languageDirectory, langOption + ".yml");

        if (!selectedLangFile.exists()) {
            selectedLangFile = new File(languageDirectory, "en-US.yml");
            plugin.getLogger().warning("Language file " + langOption + ".yml (" + selectedLangFile.getPath() + ") not found! Using fallback en-US.yml.");
        }

        plugin.getLogger().info("Using language file: " + selectedLangFile.getPath());
        langConfig = YamlConfiguration.loadConfiguration(selectedLangFile);
    }

    /**
     * Returns a string from the language file
     * @param key The key of the string
     * @return The string
     */
    public String getString(String key) {
        return langConfig.getString(key);
    }

    /**
     * Returns a string from the language file
     * @param key The key of the string
     * @param fallback The fallback string
     * @return The string
     */
    public String getString(String key, String fallback) {
        return langConfig.getString(key) != null ? langConfig.getString(key) : fallback;
    }
}

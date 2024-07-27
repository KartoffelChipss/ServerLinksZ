package org.strassburger.serverLinksZ.util;

import org.bukkit.Bukkit;
import org.bukkit.ServerLinks;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.strassburger.serverLinksZ.ServerLinksZ;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LinkManager {
    private static final ServerLinks serverLinks = Bukkit.getServer().getServerLinks();
    private static final Logger logger = ServerLinksZ.getInstance().getLogger();

    private LinkManager() {}

    /**
     * Updates the links
     */
    public static void updateLinks() {
        final FileConfiguration config = ServerLinksZ.getInstance().getConfig();
        ConfigurationSection links = config.getConfigurationSection("links");

        clearLinks();

        if (links != null) {
            for (String key : links.getKeys(false)) {
                String name = config.getString("links." + key + ".name");
                String url = config.getString("links." + key + ".url");

                registerLink(name, url);
            }
        }
    }

    /**
     * Registers a link
     * @param name The name of the link
     * @param url The URL of the link
     */
    private static void registerLink(String name, String url) {
        try {
            URI uri = new URI(url);
            serverLinks.addLink(MessageUtils.formatMsg(name), uri);
        } catch (URISyntaxException e) {
            logger.warning("Invalid URL: " + url);
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a link
     * @param key The key of the link
     * @param name The name of the link
     * @param url The URL of the link
     * @param command Whether the link should allow commands
     */
    public static void addLink(String key, String name, String url, boolean command) {
        final FileConfiguration config = ServerLinksZ.getInstance().getConfig();
        config.set("links." + key + ".name", name);
        config.set("links." + key + ".url", url);
        config.set("links." + key + ".allowCommand", command);
        ServerLinksZ.getInstance().saveConfig();
        updateLinks();
    }

    /**
     * Removes a link
     * @param key The key of the link
     */
    public static void removeLink(String key) {
        final FileConfiguration config = ServerLinksZ.getInstance().getConfig();
        config.set("links." + key, null);
        ServerLinksZ.getInstance().saveConfig();
        updateLinks();
    }

    /**
     * Clears all links from the server
     */
    public static void clearLinks() {
        for (ServerLinks.ServerLink link : serverLinks.getLinks()) {
            serverLinks.removeLink(link);
        }
    }

    /**
     * Gets all link keys from the config
     * @return All link keys
     */
    public static Set<String> getLinkKeys() {
        final FileConfiguration config = ServerLinksZ.getInstance().getConfig();
        return config.getConfigurationSection("links").getKeys(false);
    }

    /**
     * Gets all link keys from the config that match the predicate
     * @param predicate The predicate to match
     * @return All link keys that match the predicate
     */
    public static Set<String> getLinkKeys(Predicate<String> predicate) {
        return getLinkKeys().stream().filter(predicate).collect(Collectors.toSet());
    }
}

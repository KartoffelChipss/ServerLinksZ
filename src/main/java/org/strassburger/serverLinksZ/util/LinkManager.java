package org.strassburger.serverLinksZ.util;

import org.bukkit.Bukkit;
import org.bukkit.ServerLinks;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.strassburger.serverLinksZ.ServerLinksZ;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LinkManager {
    private static final ServerLinks serverLinks = Bukkit.getServer().getServerLinks();
    private static final Logger logger = ServerLinksZ.getInstance().getLogger();

    public record Link(String id, String name, String url, boolean allowCommand) {}

    private LinkManager() {}

    /**
     * Updates the links
     */
    public static void updateLinks() {
        final FileConfiguration config = getLinksConfig();

        clearLinks();

        for (String key : config.getKeys(false)) {
            String name = config.getString(key + ".name");
            String url = config.getString(key + ".url");

            registerLink(name, url);
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
        final FileConfiguration config = getLinksConfig();
        config.set(key + ".name", name);
        config.set(key + ".url", url);
        config.set(key + ".allowCommand", command);
        saveLinksConfig(config);
        updateLinks();
    }

    /**
     * Removes a link
     * @param key The key of the link
     */
    public static void removeLink(String key) {
        final FileConfiguration config = getLinksConfig();
        config.set(key, null);
        saveLinksConfig(config);
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
     * Gets a link by key
     * @param key The key of the link
     * @return The link
     */
    @Nullable
    public static Link getLink(String key) {
        final FileConfiguration config = getLinksConfig();
        String name = config.getString(key + ".name");
        String url = config.getString(key + ".url");
        boolean allowCommand = config.getBoolean(key + ".allowCommand");
        if (name == null || url == null) return null;
        return new Link(key, name, url, allowCommand);
    }

    /**
     * Gets all link keys from the config
     * @return All link keys
     */
    public static Set<String> getLinkKeys() {
        return getLinksConfig().getKeys(false);
    }

    /**
     * Gets all link keys from the config that match the predicate
     * @param predicate The predicate to match
     * @return All link keys that match the predicate
     */
    public static Set<String> getLinkKeys(Predicate<Link> predicate) {
        return getLinkKeys().stream().filter(
                key -> {
                    Link link = getLink(key);
                    return link != null && predicate.test(link);
                }
        ).collect(Collectors.toSet());
    }

    private static FileConfiguration getLinksConfig() {
        File linksFile = new File(ServerLinksZ.getInstance().getDataFolder(), "links.yml");
        if (!linksFile.exists()) {
            linksFile.getParentFile().mkdirs();
            ServerLinksZ.getInstance().saveResource("links.yml", false);
        }
        return YamlConfiguration.loadConfiguration(linksFile);
    }

    private static void saveLinksConfig(FileConfiguration config) {
        File linksFile = new File(ServerLinksZ.getInstance().getDataFolder(), "links.yml");
        try {
            config.save(linksFile);
        } catch (Exception e) {
            logger.severe("Failed to save links.yml!");
            e.printStackTrace();
        }
    }
}

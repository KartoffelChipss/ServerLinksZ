package com.zetaplugins.serverlinksz.util;

import org.bukkit.Bukkit;
import org.bukkit.ServerLinks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import com.zetaplugins.serverlinksz.ServerLinksZ;
import com.zetaplugins.serverlinksz.commands.LinkCommand;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LinkManager {
    private final ServerLinksZ plugin;

    private final ServerLinks serverLinks = Bukkit.getServer().getServerLinks();
    private final Logger logger;

    public LinkManager(ServerLinksZ plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    /**
     * Updates the links
     */
    public void updateLinks() {
        final FileConfiguration config = getLinksConfig();

        clearLinks();

        for (String key : config.getKeys(false)) {
            String name = config.getString(key + ".name");
            String url = config.getString(key + ".url");

            registerLink(name, url);
        }
    }

    /**
     * Registers a link (does not update the config)
     * @param name The name of the link
     * @param url The URL of the link
     */
    private void registerLink(String name, String url) {
        try {
            URI uri = new URI(url);
            serverLinks.addLink(MessageUtils.formatMsg(name), uri);
        } catch (URISyntaxException e) {
            logger.warning("Invalid URL: " + url);
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a link to the config and updates the links
     * @param key The key of the link
     * @param name The name of the link
     * @param url The URL of the link
     * @param command Whether the link should allow commands
     */
    public void addLink(String key, String name, String url, boolean command) {
        final FileConfiguration config = getLinksConfig();
        config.set(key + ".name", name);
        config.set(key + ".url", url);
        config.set(key + ".allowCommand", command);
        saveLinksConfig(config);
        updateLinks();
    }

    /**
     * Removes a link from the config and updates the links
     * @param key The key of the link
     */
    public void removeLink(String key) {
        final FileConfiguration config = getLinksConfig();
        config.set(key, null);
        saveLinksConfig(config);
        updateLinks();
    }

    /**
     * Clears all links from the server
     */
    public void clearLinks() {
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
    public Link getLink(String key) {
        final FileConfiguration config = getLinksConfig();
        String name = config.getString(key + ".name");
        String url = config.getString(key + ".url");
        boolean allowCommand = config.getBoolean(key + ".allowCommand");
        if (name == null || url == null) return null;
        return new Link(key, name, url, allowCommand, plugin);
    }

    /**
     * Gets all link keys from the config
     * @return All link keys
     */
    public Set<String> getLinkKeys() {
        return getLinksConfig().getKeys(false);
    }

    /**
     * Gets all link keys from the config that match the predicate
     * @param predicate The predicate to match
     * @return All link keys that match the predicate
     */
    public Set<String> getLinkKeys(Predicate<Link> predicate) {
        return getLinkKeys().stream().filter(
                key -> {
                    Link link = getLink(key);
                    return link != null && predicate.test(link);
                }
        ).collect(Collectors.toSet());
    }

    private FileConfiguration getLinksConfig() {
        File linksFile = new File(plugin.getDataFolder(), "links.yml");
        if (!linksFile.exists()) {
            linksFile.getParentFile().mkdirs();
            plugin.saveResource("links.yml", false);
        }
        return YamlConfiguration.loadConfiguration(linksFile);
    }

    private void saveLinksConfig(FileConfiguration config) {
        File linksFile = new File(plugin.getDataFolder(), "links.yml");
        try {
            config.save(linksFile);
        } catch (Exception e) {
            logger.severe("Failed to save links.yml!");
            e.printStackTrace();
        }
    }

    public record Link(String id, String name, String url, boolean allowCommand, ServerLinksZ plugin) {
        /**
         * Get a BukkitCommand for a link
         * @return The BukkitCommand
         */
        public @NotNull Command getCommand() {
            LinkCommand executor = new LinkCommand(plugin);
            return new BukkitCommand(id()) {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
                    return executor.onCommand(sender, this, commandLabel, args);
                }

                @Override
                public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
                    List<String> completions = executor.onTabComplete(sender, this, alias, args);
                    return completions == null ? List.of() : completions;
                }
            };
        }
    }
}

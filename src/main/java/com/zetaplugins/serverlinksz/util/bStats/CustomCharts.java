package com.zetaplugins.serverlinksz.util.bStats;

import com.zetaplugins.serverlinksz.ServerLinksZ;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomCharts {
    public static Metrics.CustomChart getLanguageChart(ServerLinksZ plugin) {
        return new Metrics.SimplePie("language", () -> plugin.getConfig().getString("lang"));
    }

    public static Metrics.CustomChart getLinksChart(ServerLinksZ plugin) {
        return new Metrics.AdvancedPie("links", () -> {
            Set<String> links = plugin.getLinkManager().getLinkKeys();

            Map<String, Integer> optionCounts = new HashMap<>();

            for (String link : links) {
                optionCounts.put(link, optionCounts.getOrDefault(link, 0) + 1);
            }

            return optionCounts;
        });
    }
}

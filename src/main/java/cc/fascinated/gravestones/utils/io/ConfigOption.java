package cc.fascinated.gravestones.utils.io;

import cc.fascinated.gravestones.Gravestones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/*
 * Created by NoneTaken on 24/11/2020 17:14
 */
@Getter
@AllArgsConstructor
public enum ConfigOption {

    // Settings
    ACTIVE_GRAVE_TYPE("settings.active-grave-type"),
    LOG_GRAVE_CREATION("settings.log-grave-creation"),
    CREATE_EMPTY_GRAVES("settings.create-empty-graves"),

    TYPE_CHEST_HOLOGRAM("types.CHEST.hologram");

    private final String path;

    private static final HashMap<ConfigOption, String> resultCache = new HashMap<>();
    private static final HashMap<ConfigOption, Boolean> booleanResultCache = new HashMap<>();
    private static final HashMap<ConfigOption, List<String>> resultListCache = new HashMap<>();

    /**
     * Get the setting in the config file for {@link ConfigOption#path}
     * This can also be used to refresh the {@link ConfigOption#resultCache} map
     *
     * @return - The string corresponding to this setting in the config file
     */
    public String get() {
        return resultCache.computeIfAbsent(this, val -> ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Gravestones.getINSTANCE().getGravesConfig().getConfiguration().getString(this.path))));
    }

    /**
     * Get the setting in the config file for {@link ConfigOption#path}
     * This can also be used to refresh the {@link ConfigOption#booleanResultCache} map
     *
     * @return - The boolean corresponding to this setting in the config file
     */
    public Boolean getAsBoolean() {
        return booleanResultCache.computeIfAbsent(this, val -> Gravestones.getINSTANCE().getGravesConfig().getConfiguration().getBoolean(this.path));
    }

    public List<String> getAsList() {
        return resultListCache.computeIfAbsent(this, val -> {
            List<String> newList = new ArrayList<>();
            for (String line : Gravestones.getINSTANCE().getGravesConfig().getConfiguration().getStringList(this.path)) {
                newList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            return newList;
        });
    }

    public static String getValue(ConfigOption option) {
        return option.get();
    }

}

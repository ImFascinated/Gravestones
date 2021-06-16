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
public enum LangOption {

    PREFIX("prefix"),
    NO_PERMISSION("no-permission"),
    GRAVE_LOCATED("grave-located");

    private final String path;

    private static final HashMap<LangOption, String> resultCache = new HashMap<>();
    private static final HashMap<LangOption, Boolean> booleanResultCache = new HashMap<>();
    private static final HashMap<LangOption, List<String>> resultListCache = new HashMap<>();

    /**
     * Get the setting in the config file for {@link LangOption#path}
     * This can also be used to refresh the {@link LangOption#resultCache} map
     *
     * @return - The string corresponding to this setting in the config file
     */
    public String get() {
        return resultCache.computeIfAbsent(this, val -> ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Gravestones.getINSTANCE().getLang().getConfiguration().getString(this.path))));
    }

    /**
     * Get the setting in the config file for {@link LangOption#path}
     * This can also be used to refresh the {@link LangOption#booleanResultCache} map
     *
     * @return - The boolean corresponding to this setting in the config file
     */
    public Boolean getAsBoolean() {
        return booleanResultCache.computeIfAbsent(this, val -> Gravestones.getINSTANCE().getLang().getConfiguration().getBoolean(this.path));
    }

    public List<String> getAsList() {
        return resultListCache.computeIfAbsent(this, val -> {
            List<String> newList = new ArrayList<>();
            for (String line : Gravestones.getINSTANCE().getLang().getConfiguration().getStringList(this.path)) {
                newList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            return newList;
        });
    }

    public static String getValue(LangOption option) {
        return option.get();
    }

}

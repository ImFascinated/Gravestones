package cc.fascinated.gravestones.graves;

import cc.fascinated.gravestones.Gravestones;
import cc.fascinated.gravestones.utils.io.ConfigOption;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Project: Gravestones
 * Created by Fascinated#4735 on 15/06/2021
 */
public class GraveManager implements Listener {

    @Getter private final Map<UUID, Grave> activeGraves = new HashMap<>();

    /**
     * Initializes the {@link GraveManager} and loads all of the saved graves into the {@link #activeGraves} Map
     */
    public GraveManager() {
        Gravestones.getINSTANCE().getServer().getPluginManager().registerEvents(this, Gravestones.getINSTANCE());

        FileConfiguration gravestones = Gravestones.getINSTANCE().getGravestones().getConfiguration();
        ConfigurationSection gravestoneSection = gravestones.getConfigurationSection("gravestones");
        if (gravestoneSection == null)
            return;
        for (String section : gravestoneSection.getKeys(false)) {
            ConfigurationSection playerSection = gravestones.getConfigurationSection("gravestones." + section);
            if (playerSection == null || playerSection.getKeys(false).isEmpty())
                return;
            for (String graveSection : playerSection.getKeys(false)) {
                activeGraves.put(
                        UUID.fromString(section),
                        Gravestones.getGSON().fromJson(gravestones.getString("gravestones." + section + "." + graveSection), Grave.class)
                );
            }
        }
    }

    /**
     * Creates a new {@link Grave} instance and adds it to the {@link #activeGraves} Map
     *
     * @param player The grave owner
     * @param deathLoc The location that the grave will be spawned at
     * @param type The grave type {@link GraveType}
     */
    public void createGrave(Player player, Location deathLoc, GraveType type) {
        List<ItemStack> items = new ArrayList<>();
        items.addAll(Arrays.asList(player.getInventory().getContents()));
        items.addAll(Arrays.asList(player.getInventory().getArmorContents()));

        if (items.size() == 0 && !ConfigOption.CREATE_EMPTY_GRAVES.getAsBoolean()) {
            return;
        }

        activeGraves.put(player.getUniqueId(), new Grave(
                player,
                deathLoc,
                type,
                items,
                System.currentTimeMillis() + 7200000
        ));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLoc = player.getLocation();

        createGrave(player, deathLoc, GraveType.valueOf(ConfigOption.ACTIVE_GRAVE_TYPE.get()));
    }
}

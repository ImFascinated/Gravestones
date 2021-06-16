package cc.fascinated.gravestones;

import cc.fascinated.gravestones.graves.Grave;
import cc.fascinated.gravestones.graves.GraveManager;
import cc.fascinated.gravestones.utils.io.Config;
import cc.fascinated.gravestones.utils.io.serializers.ItemStackSerializer;
import cc.fascinated.gravestones.utils.io.serializers.LocationSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Gravestones extends JavaPlugin {

    @Getter private static Gravestones INSTANCE;
    @Getter private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(Grave.class, new Grave())
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
            .create();
    private Config gravesConfig;
    private Config gravestones;
    private Config lang;

    private GraveManager graveManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.gravesConfig = new Config(this, "config.yml", null);
        this.gravesConfig.saveDefaultConfig();
        this.gravestones = new Config(this, "gravestones.yml", null);
        this.gravestones.saveDefaultConfig();
        this.lang = new Config(this, "lang.yml", null);
        this.lang.saveDefaultConfig();

        graveManager = new GraveManager();
    }

    @Override
    public void onDisable() {
        for (Grave grave : graveManager.getActiveGraves().values()) {
            OfflinePlayer owner = grave.getOwner();

            this.gravestones.getConfiguration().set("gravestones." + owner.getUniqueId() + "." + grave.getId(), GSON.toJson(grave));
        }
        gravestones.saveConfig();
    }
}

package cc.fascinated.gravestones.graves;

import cc.fascinated.gravestones.Gravestones;
import cc.fascinated.gravestones.utils.Utils;
import cc.fascinated.gravestones.utils.io.ConfigOption;
import cc.fascinated.gravestones.utils.io.Serializable;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

/**
 * Project: Gravestones
 * Created by Fascinated#4735 on 15/06/2021
 */

@Getter
public class Grave implements Serializable<Grave> {

    private UUID id;
    private OfflinePlayer owner;
    private Location location;
    private GraveType type;
    private long expiryTime;
    private List<ItemStack> items;

    public Grave() {} // For GSON

    public Grave(OfflinePlayer owner, Location location, GraveType type, List<ItemStack> items, long expiryTime) {
        this(UUID.randomUUID(), owner, location, type, items, expiryTime, false);
    }

    public Grave(OfflinePlayer owner, Location location, GraveType type, List<ItemStack> items, long expiryTime, boolean logToConsole) {
        this(UUID.randomUUID(), owner, location, type, items, expiryTime, logToConsole);
    }

    @SuppressWarnings("ALL")
    public Grave(UUID id, OfflinePlayer owner, Location location, GraveType type, List<ItemStack> items, long expiryTime, boolean logToConsole) {
        this.id = id;
        this.owner = owner;
        this.type = type;
        if (this.owner == null) {
            throw new IllegalArgumentException("The player is null");
        }
        this.location = location;

        Hologram hologram = HologramsAPI.createHologram(Gravestones.getINSTANCE(), location.clone().add(new Vector(0, 1.5, 0)));
        switch(type) {
            case CHEST: {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        hologram.clearLines();
                        for (String line : ConfigOption.TYPE_CHEST_HOLOGRAM.getAsList()) {
                            hologram.appendTextLine(line
                                    .replaceAll("%player%", owner.getName())
                                    .replaceAll("%time-left%", Utils.getTimeUntil(getExpiryTime() - System.currentTimeMillis()))
                            );
                        }
                    }
                }.runTaskTimer(Gravestones.getINSTANCE(), 0L, 20L);
            }
        }

        if (ConfigOption.LOG_GRAVE_CREATION.getAsBoolean() && logToConsole) {
            Gravestones.getINSTANCE().getLogger().info("Created gravestone for '" + owner.getName() + "' with uuid '" + this.id + "' data=" + this.toString());
        }
    }

    @Override
    public String toString() {
        return "Grave{" +
                "id=" + id +
                ", owner=" + owner +
                ", location=" + location +
                ", type=" + type +
                ", items=" + items +
                '}';
    }

    @Override
    public JsonElement serialize(Grave grave, Gson gson) {
        JsonObject object = new JsonObject();
        object.addProperty("uuid", grave.getId().toString());
        object.addProperty("owner", grave.getOwner().getUniqueId().toString());
        object.addProperty("location", gson.toJson(grave.getLocation()));
        object.addProperty("type", grave.getType().name());
        object.addProperty("items", gson.toJson(grave.getItems()));
        object.addProperty("expiryTime", grave.getExpiryTime());
        return object;
    }

    @Override
    public Grave deserialize(JsonObject data, Gson gson) {
        return new Grave(
                UUID.fromString(data.get("uuid").getAsString()),
                Bukkit.getOfflinePlayer(UUID.fromString(data.get("owner").getAsString())),
                gson.fromJson(data.get("location").getAsString(), Location.class),
                GraveType.valueOf(data.get("type").getAsString()),
                gson.fromJson(data.get("items").getAsString(), new TypeToken<List<ItemStack>>(){}.getType()),
                data.get("expiryTime").getAsLong(),
                false
        );
    }
}

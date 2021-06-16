package cc.fascinated.gravestones.utils.io.serializers;

import cc.fascinated.gravestones.utils.io.Serializable;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Project: Gravestones
 * Created by Fascinated#4735 on 15/06/2021
 */
public class LocationSerializer implements Serializable<Location> {

    @Override
    public JsonElement serialize(Location object, Gson gson) {
        JsonObject json = new JsonObject();
        json.addProperty("world", object.getWorld().getName());
        json.addProperty("x", object.getX());
        json.addProperty("y", object.getY());
        json.addProperty("z", object.getZ());
        json.addProperty("yaw", object.getYaw());
        json.addProperty("pitch", object.getPitch());
        return json;
    }

    @Override
    public Location deserialize(JsonObject data, Gson gson) {
        return new Location(
                Bukkit.getWorld(data.get("world").getAsString()),
                data.get("x").getAsDouble(),
                data.get("y").getAsDouble(),
                data.get("z").getAsDouble(),
                data.get("yaw").getAsFloat(),
                data.get("pitch").getAsFloat()
        );
    }
}
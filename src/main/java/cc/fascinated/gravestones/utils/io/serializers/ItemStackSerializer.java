package cc.fascinated.gravestones.utils.io.serializers;

import cc.fascinated.gravestones.utils.ItemUtils;
import cc.fascinated.gravestones.utils.io.Serializable;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

/**
 * Project: Gravestones
 * Created by Fascinated#4735 on 15/06/2021
 */
public class ItemStackSerializer implements Serializable<ItemStack> {

    @Override
    public JsonElement serialize(ItemStack item, Gson gson) {
        JsonObject object = new JsonObject();
        object.addProperty("stack", ItemUtils.itemToString(item));
        return null;
    }

    @Override
    public ItemStack deserialize(JsonObject data, Gson gson) {
        return ItemUtils.stringToItem(data.get("stack").getAsString());
    }
}

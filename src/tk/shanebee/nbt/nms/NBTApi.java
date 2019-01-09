package tk.shanebee.nbt.nms;

import ch.njol.skript.aliases.ItemType;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public interface NBTApi {

    Class<?> getCompoundClass();

    String getNBT(Block b);

    void setNBT(Block b, String value);

    void addNBT(Block b, String value);

    String getNBT(Entity e);

    void setNBT(Entity e, String value);

    void addNBT(Entity e, String value);

    String getNBT(ItemType i);

    void setNBT(ItemType i, String value);

    void addNBT(ItemType i, String value);

    String[] getNBTTag(String a, String b);

    boolean getEntityNoClip(Entity e);

    void setEntityNoClip(Entity e, Boolean value);

    String[] getJoinedNBT(String a, String b);
}

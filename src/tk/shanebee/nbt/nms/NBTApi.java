package tk.shanebee.nbt.nms;

import ch.njol.skript.aliases.ItemType;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
 * An API for getting/setting NBT for items, entities and tile entities
 */
public interface NBTApi {

    Class<?> getCompoundClass();

    /**
     * Used to get the NBT of a tile entity
     * Such as a furnace, hopper, beacon
     * @param b Block to get NBT from
     * @return NBT of block
     */
    String getNBT(Block b);

    /**
     * Used to set the NBT of a tile entity
     * Such as a furnace, hopper, beacon
     * @param b The block you are setting the NBT of
     * @param value The NBT string you are setting for the block
     */
    void setNBT(Block b, String value);

    /**
     * Used to add NBT to a tile entity
     * Such as a furnace, hopper, beacon
     * @param b The block you are adding NBT to
     * @param value The NBT string you are adding to the block
     */
    void addNBT(Block b, String value);

    /**
     * Used to get the NBT of an entity
     * Such as a zombie, sheep or skeleton
     * @param e The entity you are getting NBT from
     * @return The NBT string of an entity
     */
    String getNBT(Entity e);

    /**
     * Used to set the NBT of an entity
     * Such as a zombie, sheep or skeleton
     * @param e The entity you are setting NBT for
     * @param value The NBT string you are setting for the entity
     */
    void setNBT(Entity e, String value);

    /**
     * Used to add NBT to an entity
     * Such as a zombie, sheep or skeleton
     * @param e The entity you are adding NBT to
     * @param value The NBT string you are adding to the entity
     */
    void addNBT(Entity e, String value);

    /**
     * Used to get NBT from an item
     * Such as the player's tool
     * @param i The item you are getting NBT from
     * @return The NBT string you get from the item
     */
    String getNBT(ItemType i);

    /**
     * Used to set the NBT of an item
     * Such as the player's tool
     * @param i The item you are setting NBT for
     * @param value The NBT string you are setting for the item
     */
    void setNBT(ItemType i, String value);

    /**
     * Used to add NBT to an item
     * Such as a player's tool
     * @param i The item you are adding NBT to
     * @param value The NBT string you are adding to the item
     */
    void addNBT(ItemType i, String value);


    // TODO test

    String getNBT(ItemStack i);

    void addNBT(ItemStack i, String value);

    void setNBT(ItemStack i, String value);

    // TODO end test

    /**
     * Used to get a specific tag from NBT
     * @param a The tag you are getting from NBT
     * @param b The NBT you are getting the tag from
     * @return The tag string from the NBT
     */
    String[] getNBTTag(String a, String b);

    /**
     * Used to get the noClip state of an entity
     * @param e The entity you are getting the noClip state from
     * @return Boolean = Whether the entity has a noClip state or not
     */
    boolean getEntityNoClip(Entity e);

    /**
     * Used to set the noClip state of an entity
     * @param e The entity you are setting the noClip state for
     * @param value Boolean = Setting the noClip state of the entity
     */
    void setEntityNoClip(Entity e, Boolean value);


    /**
     * Used to join two or more NBT strings together
     * @param list A list of NBT strings to join
     * @return A new joined NBT string
     */
    String[] getJoinedNBTList(String... list);

}

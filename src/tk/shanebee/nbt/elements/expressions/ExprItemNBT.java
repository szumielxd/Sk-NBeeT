package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.util.coll.CollectionUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.event.Event;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.MojangsonParser;

import javax.annotation.Nullable;

@Name("NBT - Item")
@Description("NBT of an item. Supports get, set, add, delete and reset")
@Examples({"add \"{Enchantments:[{id:\"\"sharpness\"\",lvl:1}]}\" to item-nbt of player's tool",
        "set item-nbt of command sender's chestplate to \"{Yes:false,No:true}\""})
@Since("1.0.0")
public class ExprItemNBT extends SimplePropertyExpression<ItemType, String> {

    static {
        register(ExprItemNBT.class, String.class, "[item[stack]( |-)]nbt", "itemtype");
    }

    @Override
    @Nullable
    public String convert(ItemType i) {
        if (i == null || i.getMaterial() == Material.AIR) {
            return null;
        }
        NBTTagCompound nbt = CraftItemStack.asNMSCopy(i.getRandom()).getTag();
        if (nbt == null) {
            return null;
        }
        return nbt.toString();
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if (
            mode == ChangeMode.ADD ||
            mode == ChangeMode.REMOVE ||
            mode == ChangeMode.SET ||
            mode == ChangeMode.DELETE ||
            mode == ChangeMode.RESET
        ) {
            return CollectionUtils.array(String.class);
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, ChangeMode mode) {
        ItemType i = getExpr().getSingle(e);
        if (i != null && i.getMaterial() != Material.AIR) {
            net.minecraft.server.v1_13_R2.ItemStack nms = CraftItemStack.asNMSCopy(i.getRandom());
            NBTTagCompound nbt;
            switch (mode) {
                case ADD:
                    nbt = new NBTTagCompound();
                    if (nms.getTag() != null) {
                        nbt = nms.getTag();
                    }
                    try {
                        NBTTagCompound nbtv = MojangsonParser.parse(((String) delta[0]));
                        nbt.a(nbtv);
                        nms.setTag(nbt);
                    } catch (CommandSyntaxException ex) {
                        Skript.warning("NBT parse error: " + ex.getMessage());
                    }
                    break;
                case REMOVE:
                    if (nms.getTag() != null) {
                        nbt = nms.getTag();
                    } else {
                        return;
                    }
                    nbt.remove(((String) delta[0]));
                    nms.setTag(nbt);
                    break;
                case SET:
                    try {
                        nms.setTag(MojangsonParser.parse(((String) delta[0])));
                    } catch (CommandSyntaxException ex) {
                        Skript.warning("NBT parse error: " + ex.getMessage());
                    }
                    break;
                case DELETE:
                case RESET:
                    nms.setTag(new NBTTagCompound());
                    break;
                default:
                    assert false;
            }
            i.setItemMeta(CraftItemStack.asBukkitCopy(nms).getItemMeta());
        }
    }

    @Override
    protected String getPropertyName() {
        return "itemtype nbt";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}

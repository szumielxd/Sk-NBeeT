package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import tk.shanebee.nbt.NBeeT;
import tk.shanebee.nbt.nms.NBTApi;

import javax.annotation.Nullable;

@Name("NBT - Item/Entity/Block")
@Description({"NBT of items, entities or tile entities (such as a furnace, hopper, brewing stand, banner, etc) " +
        "Supports get, set, add and reset. Reset will only properly work on an item, not entities or blocks"})
@Examples({"set {_nbt} to nbt of player's tool", "add \"{Enchantments:[{id:\"\"sharpness\"\",lvl:5}]}\" to nbt of player's tool",
        "reset nbt of player's tool", "set {_nbt} to nbt of target entity", "set {_nbt} to event-entity",
        "add \"{CustomName:\"\"{\\\"\"text\\\"\":\\\"\"&bMyNewName\\\"\"}\"\"}\" to target entity",
        "add \"{RequiredPlayerRange:0s}\" to targeted block's nbt", "add \"{SpawnData:{id:\"\"minecraft:wither\"\"}}\" to nbt of clicked block"})
@Since("2.0.0")
public class ExprObjectNBT extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprObjectNBT.class, String.class, "[(entity|item|block|tile[(-| )]entity)(-| )]nbt", "block/entity/itemstack/itemtype");
    }

    @Override
    @Nullable
    public String convert(Object o) {
        NBTApi api = NBeeT.getNBTApi();
        if (o instanceof ItemStack) {
            return api.getNBT((ItemStack) o);
        } else if (o instanceof ItemType) {
            return api.getNBT((ItemType) o);
        } else if (o instanceof Entity) {
            return api.getNBT((Entity) o);
        } else if (o instanceof Block) {
            return api.getNBT((Block) o);
        }
        return null;
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if (mode == ChangeMode.ADD || mode == ChangeMode.SET || mode == ChangeMode.RESET)
            return CollectionUtils.array(String.class);
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, ChangeMode mode) {
        Object o = getExpr().getSingle(event);
        String value = delta != null ? ((String) delta[0]) : "{}";
        NBTApi api = NBeeT.getNBTApi();
        switch (mode) {
            case ADD:
                if (o instanceof ItemStack) {
                    api.addNBT((ItemStack) o, value);
                } else if (o instanceof ItemType) {
                    api.addNBT((ItemType) o, value);
                } else if (o instanceof Entity) {
                    api.addNBT((Entity) o, value);
                } else if (o instanceof Block) {
                    api.addNBT(((Block) o), value);
                }
                break;
            case SET:
            case RESET:
                if (o instanceof ItemStack) {
                    api.setNBT((ItemStack) o, value);
                } else if (o instanceof ItemType) {
                    api.setNBT((ItemType) o, value);
                } else if (o instanceof Entity) {
                    api.setNBT((Entity) o, value);
                } else if (o instanceof Block) {
                    api.setNBT(((Block) o), value);
                }
                break;
            default:
                assert false;
        }
    }

    @Override
    protected String getPropertyName() {
        return "object nbt";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

}

package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Name("CustomModelData Tag")
@Description("Set/get the CustomModelData tag for an item. (Value is an integer between 0 and 99999999) [1.14+ Only]")
@Examples({"set custom model data tag of player's tool to 3",
        "set {_test} to custom model data of player's tool"})
@Since("2.4.0")
public class ExprCustomModelData extends SimplePropertyExpression<ItemStack, Long> {

    static {
        if (Skript.isRunningMinecraft(1, 14)) {
            register(ExprCustomModelData.class, Long.class, "[custom] model data [tag]", "itemstack");
        }
    }

    @SuppressWarnings("null")
    @Override
    public Long convert(ItemStack itemStack) {
        assert itemStack.getItemMeta() != null;
        if (itemStack.getItemMeta().hasCustomModelData())
            return (long) itemStack.getItemMeta().getCustomModelData();
        else
            return 0L;
    }

    @Override
    public Class<? extends Long> getReturnType() {
        return Long.class;
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return CollectionUtils.array(Number.class);
    }

    @Override
    protected String getPropertyName() {
        return "custom model data";
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (!(delta[0] instanceof Long) || (long) delta[0] > 99999999) {
            return;
        }
        final long data = (long) delta[0];
        long newData = 0;
        long oldData = 0;
        ItemMeta meta = getExpr().getSingle(e).getItemMeta();
        assert meta != null;
        if (meta.hasCustomModelData())
            oldData = meta.getCustomModelData();
        switch (mode) {
            case ADD:
                newData = oldData + data;
                break;
            case SET:
                newData = data;
                break;
            case REMOVE:
                newData = oldData - data;
                break;
            case DELETE:
            case RESET:
            case REMOVE_ALL:
        }
        meta.setCustomModelData((int) newData);
        getExpr().getSingle(e).setItemMeta(meta);
    }

    @Override
    public String toString(Event e, boolean d) {
        return "custom model data of " + getExpr().toString(e, d);
    }

}

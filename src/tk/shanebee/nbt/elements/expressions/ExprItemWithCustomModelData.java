package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.ItemMeta;

@Name("Item with CustomModelData")
@Description("Give an item with a CustomModelData tag. (Value is an integer between 0 and 99999999) [1.14+ Only]")
@Examples({"give player a diamond sword with custom model data 2",
        "set slot 1 of inventory of player to wooden hoe with custom model data 1"})
@Since("2.4.0")
public class ExprItemWithCustomModelData extends PropertyExpression<ItemType, ItemType> {

    static {
        if (Skript.isRunningMinecraft(1, 14)) {
            Skript.registerExpression(ExprItemWithCustomModelData.class, ItemType.class, ExpressionType.PROPERTY,
                    "%itemtype% with [custom] model data [tag] %integer%");
        }
    }

    @SuppressWarnings("null")
    private Expression<Integer> data;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<ItemType>) exprs[0]);
        data = (Expression<Integer>) exprs[1];
        return true;
    }

    @Override
    protected ItemType[] get(Event e, ItemType[] source) {
        int data = this.data.getSingle(e);
        return get(source, item -> {
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(data);
            item.setItemMeta(meta);
            return item;
        });
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public String toString(Event e, boolean b) {
        return getExpr().toString(e, b) + " with custom model data " + data.toString(e, b);
    }

}

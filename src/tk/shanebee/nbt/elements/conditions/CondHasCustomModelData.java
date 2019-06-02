package tk.shanebee.nbt.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.inventory.ItemStack;

@Name("Has CustomModelData")
@Description("Check if an item has a custom model data tag")
@Examples("player's tool has custom model data")
@Since("")
public class CondHasCustomModelData extends PropertyCondition<ItemStack> {

    static {
        if (Skript.isRunningMinecraft(1, 14)) {
            register(CondHasCustomModelData.class, PropertyType.HAVE, "[custom] model data [tag]", "itemstacks");
        }
    }

    @Override
    public boolean check(ItemStack itemStack) {
        assert itemStack.getItemMeta() != null;
        return itemStack.getItemMeta().hasCustomModelData();
    }

    @Override
    protected String getPropertyName() {
        return "custom model data";
    }

}

package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;

@Name("Item without NBT")
@Description("Hides the NBT of an item, including enchantments and attributes")
@Examples({"set player's tool to player's tool without NBT", "give player 1 diamond sword of sharpness 5 with no NBT",
        "set {_tool} to player's tool with hidden NBT"})
public class ExprItemNoNBT extends SimplePropertyExpression<ItemStack, ItemStack> {

    static {
        Skript.registerExpression(ExprItemNoNBT.class, ItemStack.class, ExpressionType.PROPERTY, "%itemstacks% with(out [any]| no| hidden) NBT");
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }
    @Override
    protected String getPropertyName() {
        return "Item without NBT";
    }
    @Override
    @Nullable
    public ItemStack convert(ItemStack item) {
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        i.setItemMeta(meta);
        return i;
    }
}

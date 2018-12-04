package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;

@Name("Hidden Item Flags")
@Description("Hides the item flags on items, allowing you to make super duper custom items.")
@Examples({"set player's tool to player's tool with attribute flag hidden", "give player 1 diamond sword of sharpness 5 with enchants flag hidden",
        "set {_tool} to player's tool with all flags hidden", "give player potion of harming with potion effects flag hidden"})
@Since("1.2.1")
public class ExprHiddenFlags extends SimplePropertyExpression<ItemStack, ItemStack> {

    static {
        Skript.registerExpression(ExprHiddenFlags.class, ItemStack.class, ExpressionType.PROPERTY,
                "%itemstacks% with (0¦attribute[s]|1¦enchant[s]|2¦destroy[s]|3¦potion[ ]effect[s]|4¦unbreakable|5¦all) flag[s] hidden");
    }
    @SuppressWarnings("null")
    private Integer parse = 5;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        parse = parseResult.mark;

        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    protected String getPropertyName() {
        return "Hidden Item Flags";
    }

    @Override
    @Nullable
    public ItemStack convert(ItemStack item) {
        ItemStack i = item.clone();
        ItemMeta meta = i.getItemMeta();
        int test = parse;

        switch (test) {
            case 0:
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                break;
            case 1:
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                break;
            case 2:
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                break;
            case 3:
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                break;
            case 4:
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case 5:
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
        }
        i.setItemMeta(meta);
        return i;
    }
}

package tk.shanebee.nbt.elementsNBT.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import tk.shanebee.nbt.NBeeT;
import tk.shanebee.nbt.nms.NBTApi;

import javax.annotation.Nullable;

@Name("NBT - Tag")
@Description("Returns the value of the specified tag of the specified NBT. " +
        "Also supports getting nested tags using a semi colon as a delimiter (version 2.11.1+). " +
        "(Currently only supports get. Set may be available in the future)")
@Examples({"set {_tag} to tag \"Invulnerable\" of targeted entity's nbt",
        "send \"Tag: %tag \"\"CustomName\"\" of nbt of target entity%\" to player",
        "set {_tag} to \"Enchantments\" tag of nbt of player's tool",
        "set {_tag} to \"BlockEntityTag;Items\" tag of nbt of target block"})
@Since("1.0.0")
public class ExprTagOfNBT extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprTagOfNBT.class, String.class, ExpressionType.SIMPLE,
                "tag %string% of %string%", "%string% tag of %string%");
    }

    private Expression<String> a;
    private Expression<String> b;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.a = (Expression<String>) expressions[0];
        this.b = (Expression<String>) expressions[1];
        return true;
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        NBTApi api = NBeeT.getNBTApi();
        String t = a.getSingle(e);
        String n = b.getSingle(e);
        if (t.contains(";")) {
            return getNested(t, n);
        }
        return api.getNBTTag(t, n);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        return "Tag \"" + a.toString(e, d) + "\" of " + b.toString(e, d);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    private String[] getNested(String tag, String nbt) {
        NBTApi api = NBeeT.getNBTApi();
        String[] split = tag.split(";");
        String nbtNew = nbt;
        for (String s : split) {
            nbtNew = api.getNBTTag(s, nbtNew)[0];
        }
        return new String[]{nbtNew};
    }

}
package tk.shanebee.nbt.elementsNBT.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.event.Event;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.util.Kleenean;
import tk.shanebee.nbt.NBeeT;
import tk.shanebee.nbt.nms.NBTApi;

import javax.annotation.Nullable;

@Name("NBT - Joined")
@Description("Joins two or more different NBTs together. The latter value will overwrite conflicting values between the them.")
@Examples("set {_ex} to joined nbt from \"{Test:false,Whatever:123}\" and \"{Something:something,Test:true}\"")
@Since("1.0.0")
public class ExprJoinedNBT extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprJoinedNBT.class, String.class, ExpressionType.SIMPLE,
                "joined nbt[( |-)string] from %strings%");
    }

    private Expression<String> a;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.a = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        NBTApi api = NBeeT.getNBTApi();
        return api.getJoinedNBTList(a.getArray(e));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        return "Joined NBT from string[s] " + a.toString(e, d);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

}

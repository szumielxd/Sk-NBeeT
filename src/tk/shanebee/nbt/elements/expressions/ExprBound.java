package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import tk.shanebee.nbt.elements.objects.Bound;

@Name("Bounding Box")
@Description({"Create a new 3D bounding box between 2 points. This could be useful for creating your own region system. ",
        "Optional \"full\" bound is a bound that extends from Y=0 to Y=255 (of whatever max height your world is)."})
@Examples({"set {bound::1} to bound between {loc1} and {loc2}",
        "set {bound::2} to full bound between {loc1} and {loc2}"})
@Since("2.6.0")
public class ExprBound extends SimpleExpression<Bound> {

    static {
        Skript.registerExpression(ExprBound.class, Bound.class, ExpressionType.SIMPLE,
                "[a] [new] [(1¦full)] bound between %location% and %location%");
    }

    private Expression<Location> lesser, greater;
    private boolean full;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.lesser = (Expression<Location>) expressions[0];
        this.greater = (Expression<Location>) expressions[1];
        this.full = parseResult.mark == 1;
        return true;
    }

    @Override
    protected Bound[] get(Event event) {
        Location lesser = this.lesser.getSingle(event);
        Location greater = this.greater.getSingle(event);
        World w = greater.getWorld();
        assert w != null;
        int max = w.getMaxHeight() - 1;
        if (full) {
            lesser.setY(0);
            greater.setY(max);
        }
        return CollectionUtils.array(new Bound(lesser, greater));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Bound> getReturnType() {
        return Bound.class;
    }

    @Override
    public String toString(Event e, boolean d) {
        return "Bound between " + lesser.toString(e, d) + " and " + greater.toString(e, d);
    }

}


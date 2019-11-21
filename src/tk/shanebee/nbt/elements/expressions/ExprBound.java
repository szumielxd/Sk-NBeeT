package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
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
import tk.shanebee.nbt.elements.objects.OldBound;

@SuppressWarnings("deprecation")
@Name("Bounding Box")
@Description({"DEPRECATED... Please use new CREATE BOUND effect [Bound - Create/Remove]. ",
        "To convert old bounds to new bounds use the new convert bound effect [Bound - Convert]. ",
        "This will be removed in the future."})
@Since("2.6.0-deprecated")
public class ExprBound extends SimpleExpression<OldBound> {

    static {
        Skript.registerExpression(ExprBound.class, OldBound.class, ExpressionType.SIMPLE,
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
    protected OldBound[] get(Event event) {
        Location lesser = this.lesser.getSingle(event);
        Location greater = this.greater.getSingle(event);
        World w = greater.getWorld();
        assert w != null;
        int max = w.getMaxHeight() - 1;
        if (full) {
            lesser.setY(0);
            greater.setY(max);
        }
        return CollectionUtils.array(new OldBound(lesser, greater));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends OldBound> getReturnType() {
        return OldBound.class;
    }

    @Override
    public String toString(Event e, boolean d) {
        return "Bound between " + lesser.toString(e, d) + " and " + greater.toString(e, d);
    }

}


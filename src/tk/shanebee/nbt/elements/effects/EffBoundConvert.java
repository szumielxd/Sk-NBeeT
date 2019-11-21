package tk.shanebee.nbt.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;
import tk.shanebee.nbt.NBeeT;
import tk.shanebee.nbt.elements.objects.Bound;
import tk.shanebee.nbt.elements.objects.OldBound;

@SuppressWarnings("deprecation")
@Name("Bound - Convert")
@Description({"Convert a bound from the old format to the new format. ",
        "See the Sk-NBeeT wiki for conversion instructions: https://github.com/ShaneBeee/Sk-NBeeT/wiki/Bound-Conversion. ",
        "This will be removed in the future after enough time has passed to convert old bounds."})
@Examples("convert old bound {bound} to new bound with id \"new.bound\"")
public class EffBoundConvert extends Effect {

    static {
        Skript.registerEffect(EffBoundConvert.class,
                "convert [old] bound %oldbound% to [a] [new] bound with id %string%");
    }

    private Expression<OldBound> oldBound;
    private Expression<String> id;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.oldBound = (Expression<OldBound>) exprs[0];
        this.id = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        if (oldBound.getSingle(event) == null || id.getSingle(event) == null) {
            Skript.error("Bound or ID = null:");
            Skript.error(" " + this.toString(event, true));
            return;
        }

        OldBound oldBound = this.oldBound.getSingle(event);
        String id = this.id.getSingle(event);

        Location less = oldBound.getLesserCorner();
        Location great = oldBound.getGreaterCorner();
        Bound bound = new Bound(less, great, id);
        NBeeT.getInstance().getBoundConfig().saveBound(bound);
    }

    @Override
    public String toString(Event e, boolean d) {
        return "Convert old bound " + oldBound.toString(e, d) + " to new bound with id " + id.toString(e, d);
    }

}

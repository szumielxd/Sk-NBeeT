package tk.shanebee.nbt.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;
import tk.shanebee.nbt.NBeeT;
import tk.shanebee.nbt.nms.NBTApi;

@Name("Set Block with NBT")
public class EffSetBlockNBT extends Effect {

    static {
        Skript.registerEffect(EffSetBlockNBT.class,
                "set (nbt[(-| )]block|tile[(-| )]entity) %directions% %locations% to %itemtype% with nbt %string%");
    }

    @SuppressWarnings("null")
    private Expression<Location> locations;
    private Expression<ItemType> type;
    private Expression<String> nbtString;


    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        type = (Expression<ItemType>) exprs[2];
        locations = Direction.combine((Expression<? extends Direction>) exprs[0], (Expression<? extends Location>) exprs[1]);
        nbtString = (Expression<String>) exprs[3];
        return true;
    }

    @Override
    public void execute(final Event e) {
        NBTApi api = NBeeT.getNBTApi();
        String value = this.nbtString.getSingle(e);
        final ItemType block = type.getSingle(e);
        for (final Location loc : locations.getArray(e)) {
            assert loc != null : locations;
            block.getBlock().setBlock(loc.getBlock(), true);
            api.addNBT(loc.getBlock(), value);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set block " + locations.toString(e, debug) + " to " +
                type.toString(e, debug) + " with nbt " + nbtString.toString(e, debug);
    }
}

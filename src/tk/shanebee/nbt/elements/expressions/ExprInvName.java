package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("unused")
// This is temporary until Skript can fix getting the name of an inventory
public class ExprInvName extends SimplePropertyExpression<Object, String> {

	static {
		if (Skript.isRunningMinecraft(1, 14))
			register(ExprInvName.class, String.class, "(custom|inventory) name", "inventories");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		setExpr(exprs[0]);
		return true;
	}

	@Override
	protected String getPropertyName() {
		return "inventory name";
	}

	@Override
	public String convert(Object o) {
		if (o instanceof Inventory) {
			if (!((Inventory) o).getViewers().isEmpty())
				return ((Inventory) o).getViewers().get(0).getOpenInventory().getTitle();
			else return null;
		} else
			return null;
	}


	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(Event e, boolean d) {
		return "Inventory name of " + getExpr().toString(e, d);
	}

}

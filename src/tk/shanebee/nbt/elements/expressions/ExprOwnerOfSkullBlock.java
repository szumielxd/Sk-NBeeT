package tk.shanebee.nbt.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Skull Owner of Block")
@Description("Sets the owner of a skull block")
@Examples({"set skull owner of target block to player",
        "set skull owner of block at player to \"ShaneBee\" parsed as offline player"})
@Since("1.2.3")
public class ExprOwnerOfSkullBlock extends SimplePropertyExpression<Block, OfflinePlayer> {

    static {
        register(ExprOwnerOfSkullBlock.class, OfflinePlayer.class,"skull( |-)owner", "block");
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if ( mode == ChangeMode.SET) {
            return CollectionUtils.array(OfflinePlayer.class);
        }
        return null;
    }

    @Override
    @Nullable
    public OfflinePlayer convert(Block block) {
        BlockState state = block.getState();
        if (state instanceof Skull) {
            Skull skull = (Skull) state;
            return skull.getOwningPlayer();
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, ChangeMode mode) {
        Block block = getExpr().getSingle(e);
        if (block == null) return;
        if (mode == ChangeMode.SET) {
            OfflinePlayer player = ((OfflinePlayer) delta[0]);
            BlockState state = block.getState();
            if (block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD) {
                if (state instanceof Skull) {
                    Skull skull = ((Skull) state);
                    skull.setOwningPlayer(player);
                    skull.update();
                }
            }
        }
    }

    @Override
    protected String getPropertyName() {
        return "skull owner of block";
    }

    @Override
    public Class<? extends OfflinePlayer> getReturnType() {
        return OfflinePlayer.class;
    }
}

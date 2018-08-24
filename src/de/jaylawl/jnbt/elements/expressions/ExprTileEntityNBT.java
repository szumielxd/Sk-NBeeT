package de.jaylawl.jnbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.MojangsonParser;
import net.minecraft.server.v1_12_R1.MojangsonParseException;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.IBlockData;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import javax.annotation.Nullable;

public class ExprTileEntityNBT extends SimplePropertyExpression<Block, String> {

    static {
        register(ExprTileEntityNBT.class, String.class, "[(block|tile[entity])( |-)]nbt", "block");
    }

    @Override
    @Nullable
    public String convert(Block b) {
        net.minecraft.server.v1_12_R1.World w = ((CraftWorld) b.getWorld()).getHandle();
        net.minecraft.server.v1_12_R1.TileEntity te = w.getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ()));
        if (te == null) {
            return null;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        te.save(nbt);
        return nbt.toString();
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if (mode == ChangeMode.ADD || mode == ChangeMode.SET)
            return CollectionUtils.array(String.class);
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, ChangeMode mode){
        Block b = getExpr().getSingle(event);
        String value = ((String) delta[0]);
        NBTTagCompound nbt = new NBTTagCompound();
        net.minecraft.server.v1_12_R1.World w = ((CraftWorld) b.getWorld()).getHandle();
        net.minecraft.server.v1_12_R1.TileEntity te = w.getTileEntity(new BlockPosition(b.getX(), b.getY(), b.getZ()));
        if (te != null) {
            switch (mode) {
                case ADD:
                    te.save(nbt);
                    try {
                        NBTTagCompound nbtv = MojangsonParser.parse(value);
                        nbt.a(nbtv);
                        te.load(nbt);
                    } catch (MojangsonParseException ex) {
                        Skript.warning("NBT parse error: " + ex.getMessage());
                    }
                    break;
                case SET:
                    try {
                        NBTTagCompound nbtv = MojangsonParser.parse(value);
                        te.load(nbtv);
                    } catch (MojangsonParseException ex) {
                        Skript.warning("NBT parse error: " + ex.getMessage());
                    }
                    break;
                default:
                    assert false;
            }
            te.update();
            IBlockData tet = w.getType(new BlockPosition(b.getX(), b.getY(), b.getZ()));
            w.notify(te.getPosition(), tet, tet, 3);
        }
    }

    @Override
    protected String getPropertyName() {
        return "tile entity nbt";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}

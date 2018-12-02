package de.jaylawl.jnbt.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.util.coll.CollectionUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.MojangsonParser;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import javax.annotation.Nullable;

public class ExprEntityNBT extends SimplePropertyExpression<Entity, String> {

    static {
        register(ExprEntityNBT.class, String.class, "[entity( |-)]nbt", "entity");
    }

    @Override
    @Nullable
    public String convert(Entity e) {
        net.minecraft.server.v1_13_R2.Entity nms = ((CraftEntity) e).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        nms.c(nbt);
        return nbt.toString();
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if (mode == ChangeMode.ADD)
            return CollectionUtils.array(String.class);
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, ChangeMode mode){
        Entity e = getExpr().getSingle(event);
        if (e != null) {
            String value = ((String) delta[0]);
            switch (mode) {
                case ADD:
                    net.minecraft.server.v1_13_R2.Entity nms = ((CraftEntity) e).getHandle();
                    NBTTagCompound nbt = new NBTTagCompound();
                    nms.c(nbt);
                    try {
                        NBTTagCompound nbtv = MojangsonParser.parse(value);
                        nbt.a(nbtv);
                        nms.f(nbt);
                    } catch (CommandSyntaxException ex) {
                        Skript.warning("NBT parse error: " + ex.getMessage());
                    }
                    break;
                default:
                    assert false;
            }
        }
    }

    @Override
    protected String getPropertyName() {
        return "entity nbt";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}

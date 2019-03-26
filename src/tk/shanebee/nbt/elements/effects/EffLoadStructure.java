package tk.shanebee.nbt.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.github.shynixn.structureblocklib.bukkit.api.StructureBlockApi;
import com.github.shynixn.structureblocklib.bukkit.api.business.enumeration.StructureMirror;
import com.github.shynixn.structureblocklib.bukkit.api.business.enumeration.StructureRotation;
import com.github.shynixn.structureblocklib.bukkit.api.business.service.PersistenceStructureService;
import com.github.shynixn.structureblocklib.bukkit.api.persistence.entity.StructureSaveConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;

@Name("Structure Block: Load")
@Description("Load structure block structures that are saved on your server. 1.9.4+ ONLY")
@Examples({"load \"house\" at location of player", "load \"barn\" at location 10 infront of player",
        "paste \"house\" at location of player with rotation 90 and with mirror left to right"})
@Since("2.2.0")
public class EffLoadStructure extends Effect {

    static {
        if (Skript.isRunningMinecraft(1, 9, 4)) {
            Skript.registerEffect(EffLoadStructure.class, "(load|paste) [structure] %string% at %location% [with rotation (0¦0|1¦90|2¦180|3¦270)]",
                    "(load|paste) [structure] %string% at %location% [with rotation (0¦0|1¦90|2¦180|3¦270)] [and] [with] mirror front to back",
                    "(load|paste) [structure] %string% at %location% [with rotation (0¦0|1¦90|2¦180|3¦270)] [and] [with] mirror left to right");
        }
    }

    @SuppressWarnings("null")
    private Expression<String> name;
    private Expression<Location> loc;
    private int rotate = 0;
    private int mirror;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        loc = (Expression<Location>) exprs[1];
        rotate = parseResult.mark;
        mirror = i;
        return true;
    }

    @Override
    protected void execute(Event event) {
        PersistenceStructureService service = StructureBlockApi.INSTANCE.getStructurePersistenceService();
        String world = Bukkit.getServer().getWorlds().get(0).getName();
        final StructureSaveConfiguration saveConfig = service.createSaveConfiguration("minecraft", name.getSingle(event), world);
        switch (rotate) {
            case 0:
                saveConfig.setRotation(StructureRotation.NONE);
                break;
            case 1:
                saveConfig.setRotation(StructureRotation.ROTATION_90);
                break;
            case 2:
                saveConfig.setRotation(StructureRotation.ROTATION_180);
                break;
            case 3:
                saveConfig.setRotation(StructureRotation.ROTATION_270);
        }
        switch (mirror) {
            case 0:
                saveConfig.setMirror(StructureMirror.NONE);
                break;
            case 1:
                saveConfig.setMirror(StructureMirror.FRONT_BACK);
                break;
            case 2:
                saveConfig.setMirror(StructureMirror.LEFT_RIGHT);
        }
        boolean structureExists = service.load(saveConfig, loc.getSingle(event));
    }

    @Override
    public String toString(Event e, boolean d) {
        return "load structure " + name.toString(e, d) + " at " + loc.toString(e, d);
    }

}

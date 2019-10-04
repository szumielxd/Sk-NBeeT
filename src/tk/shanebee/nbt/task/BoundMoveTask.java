package tk.shanebee.nbt.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tk.shanebee.nbt.NBeeT;
import tk.shanebee.nbt.elements.objects.Bound;
import tk.shanebee.nbt.event.EnterBoundEvent;
import tk.shanebee.nbt.event.ExitBoundEvent;

import java.util.HashMap;
import java.util.Map;

public class BoundMoveTask extends BukkitRunnable {

    private int id;
    private Map<Player, Location> previousLocations = new HashMap<>();

    public BoundMoveTask() {
        id = this.runTaskTimer(NBeeT.getInstance(), 10, 10).getTaskId();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (previousLocations.containsKey(player)) {
                Location previous = previousLocations.get(player);
                if (player.getLocation().equals(previous)) continue;

                for (Bound bound : NBeeT.getInstance().getBounds()) {
                    if (bound.isInRegion(player.getLocation()) && !bound.isInRegion(previous)) {
                        EnterBoundEvent enterEvent = new EnterBoundEvent(bound, player);
                        Bukkit.getPluginManager().callEvent(enterEvent);
                        if (enterEvent.isCancelled()) {
                            player.teleport(previous);
                        }
                    }
                    if (!bound.isInRegion(player.getLocation()) && bound.isInRegion(previous)) {
                        ExitBoundEvent exitEvent = new ExitBoundEvent(bound, player);
                        Bukkit.getPluginManager().callEvent(exitEvent);
                        if (exitEvent.isCancelled()) {
                            player.teleport(previous);
                        }
                    }
                }
                previousLocations.put(player, player.getLocation());
            } else {
                previousLocations.put(player, player.getLocation());
            }
        }
    }

    public int getId() {
        return this.id;
    }

}

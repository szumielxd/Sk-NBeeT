package tk.shanebee.nbt.elements.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import tk.shanebee.nbt.NBeeT;

import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("unused")
public class Bound {

    private int x;
    private int y;
    private int z;
    private int x2;
    private int y2;
    private int z2;
    private String world;

    /** Create a new bounding box between 2 sets of coordinates
     * @param world World this bound is in
     * @param x x coord of 1st corner of bound
     * @param y y coord of 1st corner of bound
     * @param z z coord of 1st corner of bound
     * @param x2 x coord of 2nd corner of bound
     * @param y2 y coord of 2nd corner of bound
     * @param z2 z coord of 2nd corner of bound
     */
    public Bound(String world, int x, int y, int z, int x2, int y2, int z2) {
        this.world = world;
        this.x = Math.min(x, x2);
        this.y = Math.min(y, y2);
        this.z = Math.min(z, z2);
        this.x2 = Math.max(x, x2);
        this.y2 = Math.max(y, y2);
        this.z2 = Math.max(z, z2);
        NBeeT.getInstance().getBounds().add(this);
    }

    /** Create a new bounding box between 2 locations (must be in same world)
     * @param location Location 1
     * @param location2 Location 2
     */
    public Bound(Location location, Location location2) {
        this(Objects.requireNonNull(location.getWorld()).getName(), location.getBlockX(), location.getBlockY(),
                location.getBlockZ(), location2.getBlockX(), location2.getBlockY(), location2.getBlockZ());
    }

    /** Create a bounding box based on a serialized string from {@link #toString()}
     * @param string String to create a new bound from
     */
    public Bound(String string) {
        String[] coords = string.split(":");
        this.world = coords[0];
        this.x = Integer.parseInt(coords[1]);
        this.y = Integer.parseInt(coords[2]);
        this.z = Integer.parseInt(coords[3]);
        this.x2 = Integer.parseInt(coords[4]);
        this.y2 = Integer.parseInt(coords[5]);
        this.z2 = Integer.parseInt(coords[6]);
        NBeeT.getInstance().getBounds().add(this);
    }

    /** Check if a location is within the region of this bound
     * @param loc Location to check
     * @return True if location is within this bound
     */
    public boolean isInRegion(Location loc) {
        if (!Objects.requireNonNull(loc.getWorld()).getName().equals(world)) return false;
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        return (cx >= x && cx <= x2) && (cy >= y && cy <= y2) && (cz >= z && cz <= z2);
    }

    /** Get location of all blocks of a type within a bound
     * @param type Material type to check
     * @return ArrayList of locations of all blocks of this type in this bound
     */
    @SuppressWarnings("unused")
    public ArrayList<Location> getBlocks(Material type) {
        World w = Bukkit.getWorld(world);
        ArrayList <Location> array = new ArrayList<>();
        for (int x3 = x; x3 <= x2; x3++) {
            for (int y3 = y; y3 <= y2; y3++) {
                for (int z3 = z; z3 <= z2; z3++) {
                    assert w != null;
                    Block b = w.getBlockAt(x3, y3, z3);
                    if (b.getType() == type) {
                        array.add(b.getLocation());
                    }
                }
            }
        }
        return array;
    }

    /** Get the world of this bound
     * @return World of this bound
     */
    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    public String getWorldString() {
        return this.world;
    }

    /** Get the greater corner of this bound
     * @return Location of greater corner
     */
    public Location getGreaterCorner() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    /** Get the lesser corner of this bound
     * @return Location of lesser corner
     */
    public Location getLesserCorner() {
        return new Location(Bukkit.getWorld(world), x2, y2, z2);
    }

    /** Get the center location of this bound
     * @return The center location
     */
    public Location getCenter() {
        BoundingBox box = new BoundingBox(x, y, z, x2, y2, z2);
        return new Location(this.getWorld(), box.getCenterX(), box.getCenterY(), box.getCenterZ());
    }

    public int getLesserX() {
        return x;
    }

    public void setLesserX(int x) {
        this.x = x;
    }

    public int getLesserY() {
        return y;
    }

    public void setLesserY(int y) {
        this.y = y;
    }

    public int getLesserZ() {
        return z;
    }

    public void setLesserZ(int z) {
        this.z = z;
    }

    public int getGreaterX() {
        return x2;
    }

    public void setGreaterX(int x2) {
        this.x2 = x2;
    }

    public int getGreaterY() {
        return y2;
    }

    public void setGreaterY(int y2) {
        this.y2 = y2;
    }

    public int getGreaterZ() {
        return z2;
    }

    public void setGreaterZ(int z2) {
        this.z2 = z2;
    }

    /** Serialize this bound into a string
     * @return String including world and coordinates
     */
    public String toString() {
        return "" + world + ":" + x + ":" + y + ":" + z + ":" + x2 + ":" + y2 + ":" + z2;
    }

}

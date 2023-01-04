package de.richie93.gsinfo.counter;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.TileEntityBlock;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.Countable;
import com.sk89q.worldedit.world.block.BlockState;
import de.richie93.gsinfo.GsInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Counter {

    private final Location min;
    private final Location max;
    private String gsID;

    private Map<Material, Integer> blocks = new HashMap<>();
    private Set<TileEntityBlock> tileEntities = new HashSet<>();
    private Map<EntityType, Integer> entities = new HashMap<>();

    public Counter(Location min, Location max) {
        this.min = min;
        this.max = max;

        try (EditSession editSession = GsInfo.getWorldEdit().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(min.getWorld()), -1)) {
            CuboidRegion region2 = new CuboidRegion(BukkitAdapter.adapt(min.getWorld()),
                    BlockVector3.at(min.getBlockX(),
                            min.getBlockY(),
                            min.getBlockZ()),
                    BlockVector3.at(max.getBlockX(),
                            max.getBlockY(),
                            max.getBlockZ()));

            for(Countable<BlockState> countable : editSession.getBlockDistribution(region2, false)) {
                Material material = BukkitAdapter.adapt(countable.getID()).getMaterial();
                int count = countable.getAmount();
                blocks.put(material, count);
            }
        }

        for (Entity entity : min.getWorld().getEntities()) {
            Location location = entity.getLocation();

            if (location.getBlockX() >= min.getBlockX() &&
                    location.getBlockY() >= min.getBlockY() &&
                    location.getBlockZ() >= min.getBlockZ() &&

                    location.getBlockX() <= max.getBlockX() &&
                    location.getBlockY() <= max.getBlockY() &&
                    location.getBlockZ() <= max.getBlockZ()) {
                if (!entities.containsKey(entity.getType())) {
                    entities.put(entity.getType(), 1);
                } else {
                    entities.replace(entity.getType(), entities.get(entity.getType()) + 1);
                }
            }
        }
    }
}

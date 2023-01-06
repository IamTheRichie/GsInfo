package de.richie93.gsinfo.counter;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.Countable;
import com.sk89q.worldedit.world.block.BlockState;
import de.richie93.gsinfo.GsInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beehive;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.*;

public class Counter {

    private final Location min;
    private final Location max;
    private String gsID;

    private Map<Material, Integer> blocks = new HashMap<>();
    private Map<Material, List<org.bukkit.block.BlockState>> tileEntities = new HashMap<>();
    private Map<EntityType, List<Entity>> entities = new HashMap<>();
    private Integer beesCount = 0;

    public Counter(Location min, Location max) {
        this.min = min;
        this.max = max;

        try (EditSession editSession = GsInfo.getWorldEdit().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(min.getWorld()), -1)) {
            CuboidRegion region = new CuboidRegion(BukkitAdapter.adapt(min.getWorld()),
                    BlockVector3.at(min.getBlockX(),
                            min.getBlockY(),
                            min.getBlockZ()),
                    BlockVector3.at(max.getBlockX(),
                            max.getBlockY(),
                            max.getBlockZ()));

            countBlocks(editSession, region);
            countEntities(region);
            countTileEntities(region);
        } catch (Exception e) {
            GsInfo.getInstance().getComponentLogger().error(Component.text(e.getLocalizedMessage()));
        }
    }

    public void countEntities(final CuboidRegion region) {
        region.getChunks().forEach(vector -> {
            Location location = new Location(min.getWorld(), vector.getBlockX(), 0, vector.getBlockZ()).add(2, 2, 2);
            Arrays.stream(location.getChunk().getEntities()).toList().forEach(entity -> {
                Location entitylocation = entity.getLocation();

                if (entitylocation.getBlockX() >= min.getBlockX() &&
                        entitylocation.getBlockY() >= min.getBlockY() &&
                        entitylocation.getBlockZ() >= min.getBlockZ() &&

                        entitylocation.getBlockX() <= max.getBlockX() &&
                        entitylocation.getBlockY() <= max.getBlockY() &&
                        entitylocation.getBlockZ() <= max.getBlockZ()) {
                    if (!entities.containsKey(entity.getType())) {
                        entities.put(entity.getType(), new ArrayList<>());
                    }
                    entities.get(entity.getType()).add(entity);
                }
            });
        });
    }

    public void countBlocks(final EditSession editSession, final CuboidRegion region) {
        for (Countable<BlockState> countable : editSession.getBlockDistribution(region, false)) {
            Material material = BukkitAdapter.adapt(countable.getID()).getMaterial();
            int count = countable.getAmount();
            blocks.put(material, count);
        }
    }

    public void countTileEntities(final CuboidRegion region) {
        region.getChunks().forEach(vector -> {
            Location location = new Location(min.getWorld(), vector.getBlockX(), 0, vector.getBlockZ()).add(2, 2, 2);
            Arrays.stream(location.getChunk().getTileEntities()).toList().forEach(blockState -> {
                Location blocklocation = blockState.getLocation();

                if (blocklocation.getBlockX() >= min.getBlockX() &&
                        blocklocation.getBlockY() >= min.getBlockY() &&
                        blocklocation.getBlockZ() >= min.getBlockZ() &&

                        blocklocation.getBlockX() <= max.getBlockX() &&
                        blocklocation.getBlockY() <= max.getBlockY() &&
                        blocklocation.getBlockZ() <= max.getBlockZ()) {
                    if (!tileEntities.containsKey(blockState.getType())) {
                        tileEntities.put(blockState.getType(), new ArrayList<>());
                    }
                    tileEntities.get(blockState.getType()).add(blockState);

                    if (blockState.getType().equals(Material.BEE_NEST) || blockState.getType().equals(Material.BEEHIVE)) {
                        Beehive beehive = (Beehive) blockState;
                        beesCount += beehive.getEntityCount();
                    }
                }
            });
        });
    }

    public Map<Integer, List<EntityType>> getLivingEntitiesSorted() {
        Map<Integer, List<EntityType>> map = new HashMap<>();

        entities.forEach((entityType, entities1) -> {
            if (entityType.isAlive() && !entityType.equals(EntityType.PLAYER) && !entityType.equals(EntityType.ARMOR_STAND)) {
                if (entityType.equals(EntityType.BEE)) {
                    if (!map.containsKey(entities1.size() + beesCount))
                        map.put(entities1.size() + beesCount, new ArrayList<>());
                    map.get(entities1.size() + beesCount).add(entityType);
                } else {
                    if (!map.containsKey(entities1.size()))
                        map.put(entities1.size(), new ArrayList<>());
                    map.get(entities1.size()).add(entityType);
                }
            }
        });
        if (beesCount > 0 && !entities.containsKey(EntityType.BEE)) {
            if (!map.containsKey(beesCount))
                map.put(beesCount, new ArrayList<>());
            map.get(beesCount).add(EntityType.BEE);
        }

        return new TreeMap<>(map);
    }

    public Integer getBlockCount(final String material) {
        Integer count = 0;
        for (Material temp : Material.values()) {
            if (temp.name().toLowerCase().contains(material.toLowerCase())) {
                if (blocks.containsKey(material))
                    count += blocks.get(material);
            }
        }
        return count;
    }
}

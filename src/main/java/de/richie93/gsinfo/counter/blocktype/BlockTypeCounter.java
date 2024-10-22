package de.richie93.gsinfo.counter.blocktype;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.Countable;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockTypeCounter {
    private final List<BlockType> blockTypeList = new ArrayList<>();

    public BlockTypeCounter(Location minLocation, Location maxLocation) {
        final List<Countable<BlockState>> countableList;

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(minLocation.getWorld()))) {
            CuboidRegion region2 = new CuboidRegion(BukkitAdapter.adapt(minLocation.getWorld()),
                    BlockVector3.at(minLocation.getBlockX(),
                            minLocation.getBlockY(),
                            minLocation.getBlockZ()),
                    BlockVector3.at(maxLocation.getBlockX(),
                            maxLocation.getBlockY(),
                            maxLocation.getBlockZ()));

            countableList = editSession.getBlockDistribution(region2, false);
        }

        for(Countable<BlockState> countable : countableList) {
            Material material = BukkitAdapter.adapt(countable.getID()).getMaterial();
            int count = countable.getAmount();
            blockTypeList.add(new BlockType(material, count));
        }

        Collections.sort(blockTypeList);
    }

    public List<BlockType> getBlockTypeList() {
        return blockTypeList;
    }
}

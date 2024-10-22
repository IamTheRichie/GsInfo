package de.richie93.gsinfo.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.richie93.gsinfo.effect.EffectType;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class Region {
    ProtectedRegion region;
    final String id;
    final List<OfflinePlayer> owners = new ArrayList<>();
    final List<OfflinePlayer> members = new ArrayList<>();
    final List<EffectType> effectTypes = new ArrayList<>();

    public Region(final Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        com.sk89q.worldedit.util.Location worldeditLocation = BukkitAdapter.adapt(location);

        ApplicableRegionSet regionSet = query.getApplicableRegions(worldeditLocation);

        regionSet.getRegions().forEach(protectedRegion -> {
            if (region == null) {
                region = protectedRegion;
            } else if (region.getId().equalsIgnoreCase("__global__")) {
                region = protectedRegion;
            } else if (region.getPriority() < protectedRegion.getPriority()) {
                region = protectedRegion;
            }
        });

        if (region == null) {
            RegionManager regions = container.get(BukkitAdapter.adapt(location.getWorld()));
            region = regions.getRegion("__global__");
        }

        id = region.getId();
        region.getOwners().getUniqueIds().forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            owners.add(player);
        });
        region.getMembers().getUniqueIds().forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            members.add(player);
        });

        region.getFlag(Flags.GIVE_EFFECTS).forEach(potionEffect -> effectTypes.add(new EffectType(potionEffect.getType(), potionEffect.getAmplifier())));
    }
}

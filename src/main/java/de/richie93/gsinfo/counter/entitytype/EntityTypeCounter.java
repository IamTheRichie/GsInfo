package de.richie93.gsinfo.counter.entitytype;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

public class EntityTypeCounter {
    private final List<EntityType> entityTypeList = new ArrayList<>();


    public EntityTypeCounter(Location minLocation, Location maxLocation) {
        final Map<org.bukkit.entity.EntityType, Integer> entities = new HashMap<>();

        for (Entity entity : minLocation.getWorld().getEntities()) {
            Location location = entity.getLocation();

            if (location.getBlockX() >= minLocation.getBlockX() &&
                    location.getBlockY() >= minLocation.getBlockY() &&
                    location.getBlockZ() >= minLocation.getBlockZ() &&

                    location.getBlockX() <= maxLocation.getBlockX() &&
                    location.getBlockY() <= maxLocation.getBlockY() &&
                    location.getBlockZ() <= maxLocation.getBlockZ()) {
                if (!entities.containsKey(entity.getType())) {
                    entities.put(entity.getType(), 1);
                } else {
                    entities.replace(entity.getType(), entities.get(entity.getType()) + 1);
                }
            }
        }

        entities.forEach((entityType, integer) -> entityTypeList.add(new EntityType(entityType, integer)));

        Collections.sort(entityTypeList);
    }

    public List<EntityType> getEntityList() {
        return entityTypeList;
    }
}

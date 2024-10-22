package de.richie93.gsinfo.counter.entitytype;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class EntityType implements Comparable<EntityType> {
    private final org.bukkit.entity.EntityType type;
    private final Integer count;

    public EntityType(org.bukkit.entity.EntityType type, Integer count) {
        this.type = type;
        this.count = count;
    }

    public org.bukkit.entity.EntityType getType() {
        return type;
    }

    public Integer getCount() {
        return count;
    }

    public Component buildComponent() {
        return Component.empty().append(
                Component.text((getCount() < 10 ? "0" : "") + getCount() + " x ")
        ).append(
                Component.translatable(getType().translationKey())
        );
    }

    @Override
    public int compareTo(@NotNull EntityType o) {
        return Integer.compare(this.count, o.count);
    }
}

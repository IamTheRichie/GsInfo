package de.richie93.gsinfo.counter.blocktype;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class BlockType implements Comparable<BlockType> {
    private final Material material;
    private final Integer count;

    public BlockType(Material material, Integer count) {
        this.material = material;
        this.count = count;
    }

    public Material getMaterial() {
        return material;
    }

    public Integer getCount() {
        return count;
    }

    public Component buildComponent() {
        String key = getMaterial().getBlockTranslationKey();
        if (key == null)
            key = getMaterial().translationKey();

       return Component.empty().append(
               Component.text((getCount() < 10 ? "0" : "") + getCount() + " x ")
       ).append(
               Component.translatable(key)
       );
    }

    @Override
    public int compareTo(@NotNull BlockType o) {
        return Integer.compare(this.count, o.count);
    }
}

package de.richie93.gsinfo.effect;

import net.kyori.adventure.text.Component;
import org.bukkit.potion.PotionEffectType;

public class EffectType {

    private final PotionEffectType type;
    private final Integer amplifier;

    public EffectType(PotionEffectType type, Integer amplifier) {
        this.type = type;
        this.amplifier = amplifier;
    }

    public PotionEffectType getType() {
        return type;
    }

    public Integer getAmplifier() {
        return amplifier;
    }

    public Component buildComponent() {
        String amplifier;
        if (getAmplifier() == 0) {
            amplifier = " I";
        } else if (getAmplifier() == 1) {
            amplifier = " II";
        } else if (getAmplifier() == 2) {
            amplifier = " II";
        } else if (getAmplifier() == 3) {
            amplifier = " IV";
        } else if (getAmplifier() == 4) {
            amplifier = " V";
        } else {
            amplifier = "";
        }

        return Component.empty().append(
                Component.translatable(getType().translationKey())
        ).append(
               Component.text(amplifier)
        );
    }
}

package de.richie93.gsinfo;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.richie93.gsinfo.utils.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * is the main class of the plugin
 */
public class GsInfo extends JavaPlugin {

    private static GsInfo instance;

    private static com.sk89q.worldedit.WorldEdit worldEdit = null;
    private static WorldEditPlugin worldEditplugin;

    private static com.sk89q.worldguard.WorldGuard worldGuard = null;
    private static WorldGuardPlugin worldGuardPlugin;


    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        instance = this;

        getComponentLogger().info(Component.text("starting GsInfo...").color(NamedTextColor.GREEN));
        worldEditplugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEditplugin == null || !worldEditplugin.isEnabled()) {
            getComponentLogger().error(Component.text("The plugin WorldEdit was not found!"));
            onDisable();
            return;
        }
        worldEdit = com.sk89q.worldedit.WorldEdit.getInstance();
        getComponentLogger().info(Component.text("WorldEdit successfully initialized"));

        worldGuardPlugin = WorldGuardPlugin.inst();
        if (worldGuardPlugin == null || !worldEditplugin.isEnabled()) {
            getComponentLogger().error(Component.text("The plugin WorldGuard was not found!"));
            onDisable();
            return;
        }
        worldGuard = com.sk89q.worldguard.WorldGuard.getInstance();
        getComponentLogger().info(Component.text("WorldGuard successfully initialized"));

        getComponentLogger().info(Component.text("register all permissions..."));
        Arrays.stream(Permission.values()).toList().forEach(permission -> {
            permission.register();
        });
        getComponentLogger().info(Component.text("all permissions successfully registered..."));

        getComponentLogger().info(Component.text("...GsInfo was successfully started!").color(NamedTextColor.GREEN));
    }
    @Override
    public void onDisable() {

    }

    public static WorldEdit getWorldEdit() {
        return worldEdit;
    }

    public static WorldEditPlugin getWorldEditplugin() {
        return worldEditplugin;
    }

    public static WorldGuard getWorldGuard() {
        return worldGuard;
    }

    public static WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }

    public static GsInfo getInstance() {
        return instance;
    }
}

package de.richie93.gsinfo.utils;

import de.richie93.gsinfo.GsInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public enum Permission {
    CMD_GS_INFO("gsInfo.command.gsInfo", PermissionDefault.OP, "description"),
    CMD_SELECTED_INFO("gsInfo.command.selected", PermissionDefault.OP, "description"),
    CMD_CHUNK_INFO("gsInfo.command.chunk", PermissionDefault.OP, "description");

    final String permission;
    final PermissionDefault permissionDefault;
    final String description;

    Permission(String permission, PermissionDefault permissionDefault, String description) {
        this.permission = permission;
        this.permissionDefault = permissionDefault;
        this.description = description;
    }

    public org.bukkit.permissions.Permission permission() {
        return new org.bukkit.permissions.Permission(permission, permissionDefault);
    }

    public void register() {
        try {
            Bukkit.getPluginManager().addPermission(permission());
        } catch (Exception e) {
            GsInfo.getInstance().getComponentLogger().error(Component.text(e.getLocalizedMessage()));
        }
    }

    public boolean has(final CommandSender sender) {
        return sender.hasPermission(permission());
    }

    public boolean has(final Player player) {
        return player.hasPermission(permission());
    }
}

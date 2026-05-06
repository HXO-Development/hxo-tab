package dev.hiorcraft.nex.tab.util

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {

    private const val PREFIX = "nex.tab"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_TAB = create("$COMMAND_PREFIX.tab")
}
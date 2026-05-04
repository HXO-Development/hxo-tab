package dev.hiorcraft.nex.tab.listener

import dev.hiorcraft.nex.tab.plugin
import dev.hiorcraft.nex.tab.tablistService
import dev.slne.surf.playtime.api.paper.event.AfkStateChangeEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object PlaytimeListener : Listener {

    @EventHandler
    fun onAfkChange(event: AfkStateChangeEvent) {
        val player = Bukkit.getPlayer(event.playerUuid) ?: return
        Bukkit.getAsyncScheduler().runNow(plugin) { tablistService.formatPlayer(player) }
    }
}

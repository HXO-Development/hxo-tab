package dev.hiorcraft.nex.tab.listener

import com.github.shynixn.mccoroutine.folia.launch
import dev.hiorcraft.nex.tab.plugin
import dev.hiorcraft.nex.tab.tablistService
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerShowEntityEvent

object PlayerListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        tablistService.sendAdditions(event.player)

        plugin.launch {
            tablistService.formatPlayer(event.player)
        }
    }

    @EventHandler
    fun onShow(event: PlayerShowEntityEvent) {
        val target = event.entity as? Player ?: return

        plugin.launch {
            tablistService.formatPlayer(target)
        }
    }
}
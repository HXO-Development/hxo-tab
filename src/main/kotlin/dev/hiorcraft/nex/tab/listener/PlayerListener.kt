package dev.hiorcraft.nex.tab.listener

import dev.hiorcraft.nex.tab.plugin
import dev.hiorcraft.nex.tab.tablistService
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        tablistService.sendAdditions(player)
        Bukkit.getAsyncScheduler().runNow(plugin) { tablistService.formatPlayer(player) }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        scoreboard.teams
            .filter { it.name.startsWith("nxt_") && it.hasEntry(event.player.name) }
            .forEach { it.removeEntry(event.player.name) }
    }
}
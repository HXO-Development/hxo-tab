package dev.hiorcraft.nex.tab.service

import dev.hiorcraft.nex.tab.hook.LuckPermsHook
import dev.hiorcraft.nex.tab.plugin
import dev.hiorcraft.nex.tab.tablistConfig
import dev.hiorcraft.nex.tab.util.formatWithAdventure
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

class TablistService {
    private lateinit var task: ScheduledTask

    fun start() {
        task = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, { _ ->
            Bukkit.getOnlinePlayers().forEach { sendAdditions(it) }
        }, 0L, 1L, TimeUnit.SECONDS)
    }

    fun stop() {
        if (::task.isInitialized) task.cancel()
    }

    fun sendAdditions(player: Player) {
        player.sendPlayerListHeaderAndFooter(
            tablistConfig.header.formatWithAdventure(player),
            tablistConfig.footer.formatWithAdventure(player)
        )
    }

    fun formatPlayer(player: Player) {
        val luckPermsEnabled = Bukkit.getPluginManager().isPluginEnabled("LuckPerms")

        val prefix = if (luckPermsEnabled) LuckPermsHook.getPrefix(player) else ""
        val suffix = if (luckPermsEnabled) LuckPermsHook.getSuffix(player) else ""
        val weight = if (luckPermsEnabled) LuckPermsHook.getWeight(player) else 0

        player.playerListName(MiniMessage.miniMessage().deserialize("$prefix${player.name}$suffix"))

        val teamName = "nt_${(1000 - weight).coerceAtLeast(0).toString().padStart(4, '0')}"
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        val team = scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)
        team.addPlayer(player)
    }
}
package dev.hiorcraft.nex.tab.service

import dev.hiorcraft.nex.tab.hook.LuckPermsHook
import dev.hiorcraft.nex.tab.hook.PlaytimeHook
import dev.hiorcraft.nex.tab.plugin
import dev.hiorcraft.nex.tab.util.formatWithAdventure
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

class TablistService {

    private var task: ScheduledTask? = null

    fun start() {
        task = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, { _ ->
            Bukkit.getOnlinePlayers().forEach { sendAdditions(it) }
        }, 0L, 1L, TimeUnit.SECONDS)
    }

    fun stop() {
        task?.cancel()
        task = null
    }

    fun sendAdditions(player: Player) {
        val config = plugin.config
        val header = (config.getString("header") ?: "").formatWithAdventure(player)
        val footer = (config.getString("footer") ?: "").formatWithAdventure(player)
        player.sendPlayerListHeaderAndFooter(header, footer)
    }

    fun formatPlayer(player: Player) {
        val luckPermsEnabled = Bukkit.getPluginManager().isPluginEnabled("LuckPerms")

        val prefix = if (luckPermsEnabled) LuckPermsHook.getPrefix(player) ?: Component.empty() else Component.empty()
        val suffix = if (luckPermsEnabled) LuckPermsHook.getSuffix(player) ?: Component.empty() else Component.empty()
        val color = if (luckPermsEnabled) LuckPermsHook.getGroupColor(player) ?: NamedTextColor.WHITE else NamedTextColor.WHITE

        val coloredName = Component.text(player.name, color)
        val afkTag = getAfkTag(player)

        val tabName = Component.empty()
            .append(prefix)
            .append(coloredName)
            .append(suffix)
            .append(afkTag)

        player.playerListName(tabName)
        updateSortingTeam(player)
    }

    private fun getAfkTag(player: Player): Component {
        if (!Bukkit.getPluginManager().isPluginEnabled("surf-playtime-paper")) return Component.empty()
        if (!PlaytimeHook.isAfk(player.uniqueId)) return Component.empty()
        return Component.text(" [AFK]", NamedTextColor.GRAY)
    }

    private fun updateSortingTeam(player: Player) {
        val weight = if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            LuckPermsHook.getWeight(player)
        } else 0

        val teamName = "nxt_${(99999 - weight).toString().padStart(5, '0')}"
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard

        scoreboard.teams
            .filter { it.name.startsWith("nxt_") && it.hasEntry(player.name) }
            .forEach { it.removeEntry(player.name) }

        val team = scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)
        team.addEntry(player.name)
    }
}

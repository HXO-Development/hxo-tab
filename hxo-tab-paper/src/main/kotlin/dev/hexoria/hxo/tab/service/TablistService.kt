package dev.hexoria.hxo.tab.service

import dev.hexoria.hxo.tab.api.HxoTabApi
import dev.hexoria.hxo.tab.hook.LuckPermsHook
import dev.hexoria.hxo.tab.plugin
import dev.hexoria.hxo.tab.tablistConfig
import dev.hexoria.hxo.tab.util.formatWithAdventure
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

class TablistService : HxoTabApi {
    private lateinit var task: ScheduledTask

    fun start() {
        task = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, { _ ->
            Bukkit.getOnlinePlayers().forEach { sendAdditions(it) }
        }, 0L, 1L, TimeUnit.SECONDS)
    }

    fun stop() {
        if (::task.isInitialized) task.cancel()
    }

    override fun sendAdditions(player: Player) {
        player.sendPlayerListHeaderAndFooter(
            tablistConfig.header.formatWithAdventure(player),
            tablistConfig.footer.formatWithAdventure(player)
        )
    }

    override fun formatPlayer(player: Player) {
        val luckPermsEnabled = Bukkit.getPluginManager().isPluginEnabled("LuckPerms")

        val prefix = if (luckPermsEnabled) LuckPermsHook.getPrefix(player) else ""
        val suffix = if (luckPermsEnabled) LuckPermsHook.getSuffix(player) else ""
        val weight = if (luckPermsEnabled) LuckPermsHook.getWeight(player) else 0

        val nameResolver = TagResolver.resolver(
            TagResolver.resolver("prefix", Tag.preProcessParsed(prefix)),
            TagResolver.resolver("suffix", Tag.preProcessParsed(suffix)),
            TagResolver.resolver("player", Tag.preProcessParsed(player.name))
        )
        player.playerListName(MiniMessage.miniMessage().deserialize(tablistConfig.nameFormat, nameResolver))

        val teamName = "ht_${(1000 - weight).coerceAtLeast(0).toString().padStart(4, '0')}"
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        val team = scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)
        team.addPlayer(player)
    }
}

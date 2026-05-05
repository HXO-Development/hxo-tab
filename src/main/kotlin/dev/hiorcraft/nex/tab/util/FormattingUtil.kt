package dev.hiorcraft.nex.tab.util

import io.github.miniplaceholders.api.MiniPlaceholders
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val miniMessage = MiniMessage.miniMessage()

fun String.formatWithAdventure(player: Player? = null): Component {
    val now = ZonedDateTime.now()
    val pm = Bukkit.getPluginManager()

    var processed = this
        .replace("<server>", Bukkit.getServer().name)
        .replace("<players_online>", Bukkit.getOnlinePlayers().size.toString())
        .replace("<players_max>", Bukkit.getMaxPlayers().toString())
        .replace("<date>", now.format(dateFormatter))
        .replace("<time>", now.format(timeFormatter))
        .let { if (player != null) it.replace("<player_name>", player.name) else it }

    if (player != null && pm.isPluginEnabled("PlaceholderAPI")) {
        processed = PlaceholderAPI.setPlaceholders(player, processed)
    }

    return if (pm.isPluginEnabled("MiniPlaceholders") && player != null) {
        miniMessage.deserialize(processed, player, MiniPlaceholders.audienceGlobalPlaceholders())
    } else if (pm.isPluginEnabled("MiniPlaceholders")) {
        miniMessage.deserialize(processed, MiniPlaceholders.globalPlaceholders())
    } else {
        miniMessage.deserialize(processed)
    }
}

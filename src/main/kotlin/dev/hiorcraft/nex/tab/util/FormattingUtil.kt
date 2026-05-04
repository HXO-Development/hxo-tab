package dev.hiorcraft.nex.tab.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun String.formatWithAdventure(player: Player? = null): Component {
    val now = ZonedDateTime.now()
    val processed = this
        .replace("<server>", Bukkit.getServer().name)
        .replace("<players_online>", Bukkit.getOnlinePlayers().size.toString())
        .replace("<players_max>", Bukkit.getMaxPlayers().toString())
        .replace("<date>", now.format(dateFormatter))
        .replace("<time>", now.format(timeFormatter))

    return MiniMessage.miniMessage().deserialize(processed)
}

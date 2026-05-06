package dev.hiorcraft.nex.tab.util

import dev.hiorcraft.nex.tab.hook.LuckPermsHook
import dev.hiorcraft.nex.tab.plugin
import io.github.miniplaceholders.api.MiniPlaceholders
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val miniMessage = MiniMessage.miniMessage()
private val legacySerializer = LegacyComponentSerializer.legacySection().toBuilder()
    .character('&')
    .hexColors()
    .useUnusualXRepeatedCharacterHexFormat()
    .build()

private fun isValidServerName(value: String?): Boolean {
    if (value == null) return false
    return value.isNotBlank() && !value.equals("Unknown Server", ignoreCase = true)
}

private fun resolveServerDisplayName(): String {
    val config = plugin.config
    val fallbackOrder = config.getStringList("server-display.fallback-order")
        .ifEmpty { listOf("configured", "motd", "bukkitName") }

    val configuredName = config.getString("server-display.configured-name")
    val server = Bukkit.getServer()

    for (source in fallbackOrder) {
        val candidate = when (source.lowercase()) {
            "motd" -> server.motd
            "bukkitname" -> server.name
            "configured" -> configuredName
            else -> null
        }

        if (isValidServerName(candidate)) {
            return candidate!!
        }
    }

    return "Minecraft Server"
}

fun String.formatWithAdventure(player: Player? = null): Component {
    val now = ZonedDateTime.now()
    val pm = Bukkit.getPluginManager()
    val serverName = resolveServerDisplayName()

    var processed = this
        .replace("%prefix%", "<luckperms_prefix>")
        .replace("%suffix%", "<luckperms_suffix>")
        .replace("<server>", serverName)
        .replace("<players_online>", Bukkit.getOnlinePlayers().size.toString())
        .replace("<players_max>", Bukkit.getMaxPlayers().toString())
        .replace("<date>", now.format(dateFormatter))
        .replace("<time>", now.format(timeFormatter))
        .let { if (player != null) it.replace("<player_name>", player.name) else it }

    val resolvers = mutableListOf<TagResolver>()

    if (player != null) {
        val prefix = if (pm.isPluginEnabled("LuckPerms")) LuckPermsHook.getPrefix(player) else ""
        val suffix = if (pm.isPluginEnabled("LuckPerms")) LuckPermsHook.getSuffix(player) else ""

        resolvers += Placeholder.component(
            "luckperms_prefix",
            legacySerializer.deserialize(prefix)
        )
        resolvers += Placeholder.component(
            "luckperms_suffix",
            legacySerializer.deserialize(suffix)
        )
    }

    if (player != null && pm.isPluginEnabled("PlaceholderAPI")) {
        processed = PlaceholderAPI.setPlaceholders(player, processed)
    }

    return if (pm.isPluginEnabled("MiniPlaceholders") && player != null) {
        miniMessage.deserialize(
            processed,
            player,
            MiniPlaceholders.audienceGlobalPlaceholders(),
            TagResolver.resolver(resolvers)
        )
    } else if (pm.isPluginEnabled("MiniPlaceholders")) {
        miniMessage.deserialize(processed, MiniPlaceholders.globalPlaceholders(), TagResolver.resolver(resolvers))
    } else {
        miniMessage.deserialize(processed, TagResolver.resolver(resolvers))
    }
}

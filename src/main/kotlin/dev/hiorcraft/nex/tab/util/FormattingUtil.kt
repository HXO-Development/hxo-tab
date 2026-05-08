package dev.hiorcraft.nex.tab.util

import dev.hiorcraft.nex.tab.tablistConfig
import dev.slne.surf.api.core.messages.adventure.buildText
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private fun optionalMiniPlaceholdersResolvers(): List<TagResolver> = runCatching {
    val clazz = Class.forName("io.github.miniplaceholders.api.MiniPlaceholders")
    listOf(
        "globalPlaceholders",
        "audiencePlaceholders",
        "relationalPlaceholders",
        "relationalGlobalPlaceholders",
        "audienceGlobalPlaceholders"
    ).mapNotNull { methodName ->
        clazz.getMethod(methodName).invoke(null) as? TagResolver
    }
}.getOrDefault(emptyList())

private val globalResolver by lazy {
    val resolvers = optionalMiniPlaceholdersResolvers()
    if (resolvers.isEmpty()) {
        TagResolver.empty()
    } else {
        TagResolver.resolver(*resolvers.toTypedArray())
    }
}

private fun customResolver(): TagResolver = TagResolver.resolver(
    TagResolver.resolver("server", Tag.inserting(buildText {
        variableValue(tablistConfig.serverName)
    })),
    TagResolver.resolver("players_online", Tag.inserting(buildText {
        info(Bukkit.getOnlinePlayers().size)
    })),
    TagResolver.resolver("players_max", Tag.inserting(buildText {
        info(Bukkit.getMaxPlayers())
    })),
    TagResolver.resolver("date", Tag.inserting(buildText {
        info(ZonedDateTime.now().format(dateFormatter))
    })),
    TagResolver.resolver("time", Tag.inserting(buildText {
        info(ZonedDateTime.now().format(timeFormatter))
    }))
)

fun String.formatWithAdventure(player: Player) =
    MiniMessage.miniMessage().deserialize(
        this, player,
        TagResolver.resolver(globalResolver, customResolver())
    )
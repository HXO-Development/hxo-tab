package dev.hiorcraft.nex.tab.hook

import dev.hiorcraft.nex.tab.plugin
import dev.hiorcraft.nex.tab.tablistService
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.event.node.NodeAddEvent
import net.luckperms.api.event.node.NodeRemoveEvent
import net.luckperms.api.event.user.UserDataRecalculateEvent
import net.luckperms.api.model.user.User
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object LuckPermsHook {

    private val luckPerms by lazy { LuckPermsProvider.get() }

    fun getPrefix(player: Player): Component? {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return null
        val prefix = user.cachedData.metaData.prefix ?: return null
        return MiniMessage.miniMessage().deserialize(prefix)
    }

    fun getSuffix(player: Player): Component? {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return null
        val suffix = user.cachedData.metaData.suffix ?: return null
        return MiniMessage.miniMessage().deserialize(suffix)
    }

    fun getGroupColor(player: Player): TextColor? {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return null
        val colorStr = user.cachedData.metaData.getMetaValue("color") ?: return null
        return TextColor.fromHexString(colorStr)
            ?: NamedTextColor.NAMES.value(colorStr.lowercase())
    }

    fun getWeight(player: Player): Int {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return 0
        val group = luckPerms.groupManager.getGroup(user.primaryGroup) ?: return 0
        return group.weight.orElse(0)
    }

    fun load() {
        val eventBus = luckPerms.eventBus

        eventBus.subscribe(plugin, NodeAddEvent::class.java) { event ->
            val target = event.target as? User ?: return@subscribe
            val player = Bukkit.getPlayer(target.uniqueId) ?: return@subscribe
            Bukkit.getAsyncScheduler().runNow(plugin) { tablistService.formatPlayer(player) }
        }

        eventBus.subscribe(plugin, NodeRemoveEvent::class.java) { event ->
            val target = event.target as? User ?: return@subscribe
            val player = Bukkit.getPlayer(target.uniqueId) ?: return@subscribe
            Bukkit.getAsyncScheduler().runNow(plugin) { tablistService.formatPlayer(player) }
        }

        eventBus.subscribe(plugin, UserDataRecalculateEvent::class.java) { event ->
            val player = Bukkit.getPlayer(event.user.uniqueId) ?: return@subscribe
            Bukkit.getAsyncScheduler().runNow(plugin) { tablistService.formatPlayer(player) }
        }
    }
}
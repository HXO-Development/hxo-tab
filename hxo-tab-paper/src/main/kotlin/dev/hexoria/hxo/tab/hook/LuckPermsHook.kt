package dev.hexoria.hxo.tab.hook

import dev.hexoria.hxo.tab.plugin
import dev.hexoria.hxo.tab.redisApi
import dev.hexoria.hxo.tab.redis.event.TabEntryUpdateRedisEvent
import dev.hexoria.hxo.tab.tablistService
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.event.node.NodeAddEvent
import net.luckperms.api.event.node.NodeRemoveEvent
import net.luckperms.api.event.user.UserDataRecalculateEvent
import net.luckperms.api.model.user.User
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object LuckPermsHook {

    private val luckPerms by lazy { LuckPermsProvider.get() }

    fun getWeight(player: Player): Int {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return 0
        val group = luckPerms.groupManager.getGroup(user.primaryGroup) ?: return 0
        return group.weight.orElse(0)
    }

    fun getPrefix(player: Player): String {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return ""
        return user.cachedData.metaData.prefix ?: ""
    }

    fun getSuffix(player: Player): String {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return ""
        return user.cachedData.metaData.suffix ?: ""
    }

    private fun schedulePlayerReformat(player: Player) {
        player.scheduler.run(plugin, { _ -> tablistService.formatPlayer(player) }, null)
        redisApi.publishEvent(TabEntryUpdateRedisEvent(player.uniqueId))
    }

    fun load() {
        val eventBus = luckPerms.eventBus

        eventBus.subscribe(plugin, NodeAddEvent::class.java) { event ->
            val target = event.target as? User ?: return@subscribe
            val player = Bukkit.getPlayer(target.uniqueId) ?: return@subscribe
            schedulePlayerReformat(player)
        }

        eventBus.subscribe(plugin, NodeRemoveEvent::class.java) { event ->
            val target = event.target as? User ?: return@subscribe
            val player = Bukkit.getPlayer(target.uniqueId) ?: return@subscribe
            schedulePlayerReformat(player)
        }

        eventBus.subscribe(plugin, UserDataRecalculateEvent::class.java) { event ->
            val player = Bukkit.getPlayer(event.user.uniqueId) ?: return@subscribe
            schedulePlayerReformat(player)
        }
    }
}

package dev.hexoria.hxo.tab.redis

import com.github.shynixn.mccoroutine.folia.launch
import dev.hexoria.hxo.tab.plugin
import dev.hexoria.hxo.tab.redis.event.TabEntryUpdateRedisEvent
import dev.hexoria.hxo.tab.tablistService
import dev.slne.surf.redis.event.OnRedisEvent
import org.bukkit.Bukkit

object TabRedisEventListener {
    @OnRedisEvent
    fun onUpdate(event: TabEntryUpdateRedisEvent) {
        val player = Bukkit.getPlayer(event.toUpdateUuid) ?: return

        plugin.launch {
            tablistService.formatPlayer(player)
        }
    }
}
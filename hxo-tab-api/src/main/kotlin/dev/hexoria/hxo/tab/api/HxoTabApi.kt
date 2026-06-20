package dev.hexoria.hxo.tab.api

import org.bukkit.entity.Player

interface HxoTabApi {
    fun sendAdditions(player: Player)
    fun formatPlayer(player: Player)

    companion object {
        private var instance: HxoTabApi? = null

        fun get(): HxoTabApi = checkNotNull(instance) { "HxoTabApi is not initialized" }

        fun set(api: HxoTabApi?) {
            instance = api
        }
    }
}

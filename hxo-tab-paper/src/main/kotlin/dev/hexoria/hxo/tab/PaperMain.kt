package dev.hexoria.hxo.tab

import dev.hexoria.hxo.tab.api.HxoTabApi
import dev.hexoria.hxo.tab.command.hxoTabCommand
import dev.hexoria.hxo.tab.config.TablistConfigProvider
import dev.hexoria.hxo.tab.hook.LuckPermsHook
import dev.hexoria.hxo.tab.listener.PlayerListener
import dev.hexoria.hxo.tab.service.TablistService
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: PaperMain
    private set

val tablistService get() = plugin.tablistService
val tablistConfigProvider get() = plugin.tablistConfigProvider
val tablistConfig get() = tablistConfigProvider.config

class PaperMain : JavaPlugin() {

    lateinit var tablistService: TablistService
        private set

    lateinit var tablistConfigProvider: TablistConfigProvider
        private set

    override fun onEnable() {
        plugin = this

        tablistConfigProvider = TablistConfigProvider()
        tablistService = TablistService()

        HxoTabApi.set(tablistService)

        val pm = server.pluginManager

        if (pm.isPluginEnabled("LuckPerms")) {
            LuckPermsHook.load()
        }

        pm.registerEvents(PlayerListener, this)
        tablistService.start()
        redisLoader.connect()
        hxoTabCommand()
    }

    override fun onDisable() {
        tablistService.stop()
        redisLoader.disconnect()
        HxoTabApi.set(null)
    }
}

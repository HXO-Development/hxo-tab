package dev.hiorcraft.nex.tab

import dev.hiorcraft.nex.tab.command.nexTabCommand
import dev.hiorcraft.nex.tab.config.TablistConfigProvider
import dev.hiorcraft.nex.tab.hook.LuckPermsHook
import dev.hiorcraft.nex.tab.listener.PlayerListener
import dev.hiorcraft.nex.tab.service.TablistService
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

        val pm = server.pluginManager

        if (pm.isPluginEnabled("LuckPerms")) {
            LuckPermsHook.load()
        }

        pm.registerEvents(PlayerListener, this)
        tablistService.start()
        nexTabCommand()
    }

    override fun onDisable() {
        tablistService.stop()
    }
}
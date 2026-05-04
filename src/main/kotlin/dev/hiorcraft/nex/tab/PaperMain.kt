package dev.hiorcraft.nex.tab

import dev.hiorcraft.nex.tab.command.NexTabCommand
import dev.hiorcraft.nex.tab.hook.LuckPermsHook
import dev.hiorcraft.nex.tab.listener.PlayerListener
import dev.hiorcraft.nex.tab.listener.PlaytimeListener
import dev.hiorcraft.nex.tab.service.TablistService
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: PaperMain
    private set

val tablistService get() = plugin.tablistService

class PaperMain : JavaPlugin() {

    lateinit var tablistService: TablistService
        private set

    override fun onEnable() {
        plugin = this
        saveDefaultConfig()

        tablistService = TablistService()

        val pm = server.pluginManager

        if (pm.isPluginEnabled("LuckPerms")) {
            LuckPermsHook.load()
        }

        if (pm.isPluginEnabled("surf-playtime-paper")) {
            pm.registerEvents(PlaytimeListener, this)
        }

        pm.registerEvents(PlayerListener, this)
        tablistService.start()
        NexTabCommand.register(this)
    }

    override fun onDisable() {
        tablistService.stop()
    }
}

package dev.hiorcraft.nex.tab

import dev.hiorcraft.nex.tab.command.nexTabCommand
import dev.hiorcraft.nex.tab.hook.LuckPermsHook
import dev.hiorcraft.nex.tab.listener.PlayerListener
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

        pm.registerEvents(PlayerListener, this)
        tablistService.start()
        nexTabCommand()
    }

    override fun onDisable() {
        tablistService.stop()
    }
}

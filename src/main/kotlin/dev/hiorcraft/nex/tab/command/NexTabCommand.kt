package dev.hiorcraft.nex.tab.command

import com.mojang.brigadier.Command
import dev.hiorcraft.nex.tab.plugin
import dev.hiorcraft.nex.tab.tablistService
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object NexTabCommand {

    fun register(plugin: JavaPlugin) {
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("nex-tab")
                    .requires { it.sender.hasPermission("nex.tab.command.reload") }
                    .then(
                        Commands.literal("reload")
                            .executes { ctx ->
                                plugin.reloadConfig()
                                Bukkit.getAsyncScheduler().runNow(plugin) { _ ->
                                    Bukkit.getOnlinePlayers().forEach { player ->
                                        tablistService.sendAdditions(player)
                                        tablistService.formatPlayer(player)
                                    }
                                }
                                ctx.source.sender.sendMessage(
                                    Component.text("Die Tablist wurde neu geladen.", NamedTextColor.GREEN)
                                )
                                Command.SINGLE_SUCCESS
                            }
                    )
                    .build(),
                "Nex Tab Verwaltung"
            )
        }
    }
}

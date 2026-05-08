package dev.hiorcraft.nex.tab.command

import com.github.shynixn.mccoroutine.folia.launch
import dev.hiorcraft.nex.tab.plugin
import dev.hiorcraft.nex.tab.tablistConfigProvider
import dev.hiorcraft.nex.tab.tablistService
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.slne.surf.api.core.messages.adventure.sendText
import org.bukkit.Bukkit

fun nexTabCommand() = commandTree("nexTab") {
    withPermission("nex.tab.command.nextab")

    literalArgument("reload") {
        anyExecutor { executor, _ ->
            tablistConfigProvider.reload()

            plugin.launch {
                Bukkit.getOnlinePlayers().forEach {
                    tablistService.sendAdditions(it)
                    tablistService.formatPlayer(it)
                }
            }

            executor.sendText {
                appendSuccessPrefix()
                success("Die Tablist wurde neu geladen.")
            }
        }
    }
}
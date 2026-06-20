package dev.hexoria.hxo.tab.command

import com.github.shynixn.mccoroutine.folia.launch
import dev.hexoria.hxo.tab.plugin
import dev.hexoria.hxo.tab.tablistConfigProvider
import dev.hexoria.hxo.tab.tablistService
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.slne.surf.api.core.messages.adventure.sendText
import org.bukkit.Bukkit

fun hxoTabCommand() = commandTree("hxoTab") {
    withPermission("hxo.tab.command.hxotab")

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

package dev.hexoria.hxo.tab.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class TablistConfig(
    val header: String = "<br><br><br>\uE00B<br><br>",
    val footer: String = "<br><#59CCF2>Du bist auf <server><br><gray>ʜᴇxᴏʀɪᴀ.ɴᴇᴛ<br>",
    val nameFormat: String = "<prefix><player><suffix>"
)

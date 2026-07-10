package dev.hexoria.hxo.tab

import dev.hexoria.hxo.tab.redis.TabRedisEventListener
import dev.slne.surf.redis.RedisApi

val redisLoader = RedisLoader()
val redisApi get() = redisLoader.redisApi

class RedisLoader {
    lateinit var redisApi: RedisApi
        private set

    fun connect() {
        redisApi = RedisApi.create()
        redisApi.subscribeToEvents(TabRedisEventListener)
        redisApi.freezeAndConnect()
    }

    fun disconnect() {
        redisApi.disconnect()
    }
}
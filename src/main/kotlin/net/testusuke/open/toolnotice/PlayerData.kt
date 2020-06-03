package net.testusuke.open.toolnotice

import org.bukkit.entity.Player
import java.util.*

object PlayerData {

    //  採掘停止するかどうか
    private var playerBreakMap = mutableMapOf<UUID,Boolean>()
    fun get(player: Player):Boolean{
        return playerBreakMap[player.uniqueId] ?: false
    }
    fun set(player: Player,boolean: Boolean){
        playerBreakMap[player.uniqueId] = boolean
    }
    fun clear(){
        playerBreakMap.clear()
    }
    fun contains(player: Player): Boolean {
        return playerBreakMap.containsKey(player.uniqueId)
    }
}
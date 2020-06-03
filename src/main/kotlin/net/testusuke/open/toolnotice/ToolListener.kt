package net.testusuke.open.toolnotice

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import net.testusuke.open.toolnotice.Main.Companion.enabled
import net.testusuke.open.toolnotice.Main.Companion.prefix
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack

object ToolListener: Listener {

    @EventHandler
    fun onItemDamage(event:PlayerItemDamageEvent){
        if(!enabled)return
        val player = event.player
        val item = event.item
        val max = item.type.maxDurability
        val now =  max - item.durability
        if(max * 0.05 > now){
            damageWarning(player,item,now)
        }else if (max * 0.2 > now){
            damageAttention(player,item,now)
        }
    }

    private fun damageAttention(player:Player, item:ItemStack, now: Int){
        val material = item.type.name
        val message = "§e§l注意! ツール名: $material 耐久値: $now"
        val component = TextComponent()
        component.text = message
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,component)
    }
    private fun damageWarning(player:Player, item:ItemStack, now: Int){
        val material = item.type.name
        val message = "§c§l警告! ツール名: $material 耐久値: $now"
        val component = TextComponent()
        component.text = message
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,component)
        //  title
        player.sendTitle("§c§l警告","§c耐久値が少なくなっています。",5,40,20)
    }

    private val toolMaterial:MutableList<Material> by lazy {
        val mutableList = mutableListOf<Material>()
        //  Material
        //  AXE
        mutableList.add(Material.WOODEN_AXE)
        mutableList.add(Material.STONE_AXE)
        mutableList.add(Material.IRON_AXE)
        mutableList.add(Material.GOLDEN_AXE)
        mutableList.add(Material.DIAMOND_AXE)
        //  PICKAXE
        mutableList.add(Material.WOODEN_PICKAXE)
        mutableList.add(Material.STONE_PICKAXE)
        mutableList.add(Material.IRON_PICKAXE)
        mutableList.add(Material.GOLDEN_PICKAXE)
        mutableList.add(Material.DIAMOND_PICKAXE)
        //  shovel
        mutableList.add(Material.WOODEN_SHOVEL)
        mutableList.add(Material.STONE_SHOVEL)
        mutableList.add(Material.IRON_SHOVEL)
        mutableList.add(Material.GOLDEN_SHOVEL)
        mutableList.add(Material.DIAMOND_SHOVEL)

        mutableList
    }
    @EventHandler
    fun onBlockBreak(event:BlockBreakEvent){
        if(!enabled)return
        val player = event.player
        if(!PlayerData.get(player))return
        val item:ItemStack = player.inventory.itemInMainHand
        val material = item.type
        if(material.isAir)return
        if(!toolMaterial.contains(material))return
        val max = item.type.maxDurability
        val now =  max - item.durability
        if(now <= 2){
            event.isCancelled = true
            val message = "§c§l耐久値が低いためストッパーが作動しました!"
            val component = TextComponent()
            component.text = message
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,component)
        }
    }

    @EventHandler
    fun onPlayerJoin(event:PlayerJoinEvent){
        if(!enabled)return
        val player = event.player
        if(PlayerData.contains(player)){
            val b = PlayerData.get(player)
            val s = if(b){
                "有効"
            }else{
                "無効"
            }
            player.sendMessage("${prefix}§aストッパーは${s}になっています。")
            return
        }
        player.sendMessage("${prefix}§aストッパーは無効になっています。")
        PlayerData.set(player,false)
    }
}
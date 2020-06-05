package net.testusuke.open.toolnotice

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import net.testusuke.open.toolnotice.Main.Companion.enabled
import net.testusuke.open.toolnotice.Main.Companion.plugin
import net.testusuke.open.toolnotice.Main.Companion.prefix
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object ToolListener : Listener {

    @EventHandler
    fun onItemDamage(event: PlayerItemDamageEvent) {
        if (!enabled) return
        val player = event.player
        val item = event.item
        val max = item.type.maxDurability
        val now = max - item.durability
        if (max * 0.05 > now) {
            damageWarning(player, item, now)
        } else if (max * 0.2 > now) {
            damageAttention(player, item, now)
        }
    }

    private fun damageAttention(player: Player, item: ItemStack, now: Int) {
        val material = item.type.name
        val message = "§e§l注意! ツール名: $material 耐久値: $now"
        val component = TextComponent()
        component.text = message
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
    }

    private fun damageWarning(player: Player, item: ItemStack, now: Int) {
        val material = item.type.name
        val message = "§c§l警告! ツール名: $material 耐久値: $now"
        val component = TextComponent()
        component.text = message
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
        //  title
        player.sendTitle("§c§l警告", "§c耐久値が少なくなっています。", 5, 40, 20)
    }

    private val toolMaterial: MutableList<Material> by lazy {
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

    /**
     * Material -> ToolType
     */
    private val materialOfTool: MutableMap<Material, ToolType> by lazy {
        val mutableMap = mutableMapOf<Material, ToolType>()
        mutableMap[Material.WOODEN_PICKAXE] = ToolType.PICKAXE
        mutableMap[Material.STONE_PICKAXE] = ToolType.PICKAXE
        mutableMap[Material.IRON_PICKAXE] = ToolType.PICKAXE
        mutableMap[Material.GOLDEN_PICKAXE] = ToolType.PICKAXE
        mutableMap[Material.DIAMOND_PICKAXE] = ToolType.PICKAXE

        mutableMap[Material.WOODEN_AXE] = ToolType.AXE
        mutableMap[Material.STONE_AXE] = ToolType.AXE
        mutableMap[Material.IRON_AXE] = ToolType.AXE
        mutableMap[Material.GOLDEN_AXE] = ToolType.AXE
        mutableMap[Material.DIAMOND_AXE] = ToolType.AXE

        mutableMap[Material.WOODEN_SHOVEL] = ToolType.SHOVEL
        mutableMap[Material.STONE_SHOVEL] = ToolType.SHOVEL
        mutableMap[Material.IRON_SHOVEL] = ToolType.SHOVEL
        mutableMap[Material.GOLDEN_SHOVEL] = ToolType.SHOVEL
        mutableMap[Material.DIAMOND_SHOVEL] = ToolType.SHOVEL

        mutableMap
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!enabled) return
        val player = event.player
        if (!PlayerData.get(player)) return
        val item: ItemStack = player.inventory.itemInMainHand
        val itemMaterial = item.type
        if (itemMaterial.isAir) return
        if (!toolMaterial.contains(itemMaterial)) return
        val max = item.type.maxDurability
        val now = max - item.durability
        if (now <= 2) {
            event.isCancelled = true
            val message = "§c§l耐久値が低いためストッパーが作動しました!"
            val component = TextComponent()
            component.text = message
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component)
            //  autochange
            if (plugin.autoChangeEnabled) {
                val toolType:ToolType = materialOfTool[itemMaterial] ?: return
                val materialList = toolType.getMaterialList()
                val inv = player.inventory
                inv.setItemInMainHand(ItemStack(Material.AIR))

                var discovered = false
                for(material in materialList){
                    if(discovered){
                        break
                    }
                    if(inv.contains(material)){
                        val itemStackList = getItemStackListFromMaterial(inv,material)
                        for (itemInInventory in itemStackList){
                            val maxInInventory = item.type.maxDurability
                            val nowInInventory = maxInInventory - item.durability
                            if(nowInInventory > 2){
                                inv.setItemInMainHand(itemInInventory)
                                inv.addItem(item)
                                discovered = true
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getItemStackListFromMaterial(inventory:Inventory,material:Material):MutableList<ItemStack> {
        val list = mutableListOf<ItemStack>()
        for(item in inventory.contents){
            if(item.type == material){
                list.add(item)
            }
        }
        return list
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!enabled) return
        val player = event.player
        if (PlayerData.contains(player)) {
            val b = PlayerData.get(player)
            val s = if (b) {
                "有効"
            } else {
                "無効"
            }
            player.sendMessage("${prefix}§aストッパーは${s}になっています。")
            return
        }
        player.sendMessage("${prefix}§aストッパーは無効になっています。")
        PlayerData.set(player, false)
    }
}
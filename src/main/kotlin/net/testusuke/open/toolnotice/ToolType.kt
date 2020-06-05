package net.testusuke.open.toolnotice

import org.bukkit.Material

enum class ToolType {


    PICKAXE{
        override fun getMaterialList(): MutableList<Material> {
            return mutableListOf(
                Material.DIAMOND_PICKAXE,
                Material.GOLDEN_PICKAXE,
                Material.IRON_PICKAXE,
                Material.STONE_PICKAXE,
                Material.WOODEN_PICKAXE
            )
        }
    },
    AXE{
        override fun getMaterialList(): MutableList<Material> {
            return mutableListOf(
                Material.DIAMOND_AXE,
                Material.GOLDEN_AXE,
                Material.IRON_AXE,
                Material.STONE_AXE,
                Material.WOODEN_AXE
            )
        }
    },
    SHOVEL{
        override fun getMaterialList(): MutableList<Material> {
            return mutableListOf(
                Material.DIAMOND_SHOVEL,
                Material.GOLDEN_SHOVEL,
                Material.IRON_SHOVEL,
                Material.STONE_SHOVEL,
                Material.WOODEN_SHOVEL
            )
        }
    };
    abstract fun getMaterialList():MutableList<Material>


}
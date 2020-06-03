package net.testusuke.open.toolnotice

import net.testusuke.open.toolnotice.Main.Companion.enabled
import net.testusuke.open.toolnotice.Main.Companion.plugin
import net.testusuke.open.toolnotice.Main.Companion.prefix
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object NoticeCommand:CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player || !sender.hasPermission(plugin.generalPermission))return false

        if(args.isEmpty()){
            changeBreakMode(sender)
            return false
        }
        when(args[0]){
            "change" -> changeBreakMode(sender)
            "help" -> sendHelp(sender)
            "on" -> changeEnabled(sender,true)
            "off" -> changeEnabled(sender,false)
            "reload" -> {
                if(sender.hasPermission(plugin.adminPermission)){
                    sender.sendMessage("${prefix}§aコンフィグを再読み込みしました。")
                    plugin.reloadConfig()
                    return true
                }else{
                    sender.sendMessage("${prefix}§cあなたには権限がありません。")
                    return true
                }
            }
        }
        return false
    }

    private fun changeEnabled(player: Player,boolean: Boolean){
        if(!player.hasPermission(plugin.adminPermission)){
            player.sendMessage("${prefix}§cあなたには権限がありません。")
            return
        }
        if(enabled == boolean){
            player.sendMessage("${prefix}§cすでに${boolean}になっています。")
        }
        enabled = boolean
        player.sendMessage("${prefix}§aプラグインを${boolean}に変更しました。")
    }

    private fun sendHelp(player: Player){
        player.sendMessage("§e===================================")
        player.sendMessage("§6/tn [ToolNotice] <- ストッパーを有効/無効にします。")
        player.sendMessage("§6/tn help <- ヘルプの表示")
        player.sendMessage("§6/tn change <- ストッパーを有効/無効にします。")
        if(player.hasPermission(plugin.adminPermission)){
            player.sendMessage("§c/tn on <-　プラグインを有効にします。")
            player.sendMessage("§c/tn off <- プラグインを無効にします。")
            player.sendMessage("§c/tn reload <- コンフィグを再読み込みします。")
        }
        player.sendMessage("§d§lCreated by testusuke")
        player.sendMessage("§e===================================")
    }

    private fun changeBreakMode(player: Player){
        val boolean = PlayerData.get(player)
        if(boolean){
            PlayerData.set(player,false)
            player.sendMessage("${prefix}§aストッパーを§c無効§aにしました。")
        }else{
            PlayerData.set(player,true)
            player.sendMessage("${prefix}§aストッパーを§6有効§aにしました。")
        }
    }

}
/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.pd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * PLドヤァプラグイン
 * @author ucchy
 */
public class PLDoya extends JavaPlugin implements Listener {

    private static final String DOYA = "( ・´ｰ・｀)ドヤァ...";
    private static final String MESSAGE_FILE_NAME = "message.txt";
    
    private String message;
    
    /**
     * プラグインが有効化されたときに呼び出されるメソッド
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {
        
        // versionコマンドとpluginsコマンドの権限を絞る
        getServer().getPluginManager().addPermission(new Permission("bukkit.command.version", PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission("bukkit.command.plugins", PermissionDefault.OP));
        
        // イベントリスナー登録
        getServer().getPluginManager().registerEvents(this, this);
        
        // メッセージファイルのロード
        message = loadMessage();
    }
    
    /**
     * プレイヤーがコマンドを打った時に、実行される直前で呼び出されるメソッド
     * @param event 
     */
    @EventHandler
    public void onPlayerCommadPreprocess(PlayerCommandPreprocessEvent event) {
        
        Player player = event.getPlayer();
        String[] commands = event.getMessage().trim().split(" ");
        
        if ( commands.length == 0 ) return;
        
        String command = commands[0];
        
        if ( command.equalsIgnoreCase("/version") || 
                command.equalsIgnoreCase("/ver") || 
                command.equalsIgnoreCase("/about") ) {
            if ( !player.hasPermission("bukkit.command.version") ) {
                player.sendMessage(ChatColor.RED + message);
                event.setCancelled(true);
            }
        } else if ( command.equalsIgnoreCase("/plugins") || 
                command.equalsIgnoreCase("/pl") ) {
            if ( !player.hasPermission("bukkit.command.plugins") ) {
                player.sendMessage(ChatColor.RED + message);
                event.setCancelled(true);
            }
        }
    }
    
    /**
     * メッセージ設定ファイルからメッセージを取得する
     * @return メッセージ
     */
    private String loadMessage() {
        
        // プラグインフォルダが存在しない場合は作成する
        File dir = getDataFolder();
        if ( !dir.exists() ) {
            dir.mkdirs();
        }
        
        File file = new File(dir, MESSAGE_FILE_NAME);
        
        // message.txt が存在しない場合は、新規作成する。
        if ( !file.exists() ) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(DOYA);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if ( writer != null ) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        // do nothing.
                    }
                }
            }
        }
        
        // ファイルを読み込みする
        String message = DOYA;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            message = reader.readLine(); // 最初の1行だけ読む
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( reader != null ) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
        }
        
        return message;
    }
}

package io.github.supurazako.tpplayer;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.ResourceBundle;

public class TPPlayerCommand implements CommandExecutor {


    private TPPlayer tpPlayer;

    public TPPlayerCommand(TPPlayer tpPlayerA) {
        this.tpPlayer = tpPlayerA;
    }


    private static final String RESOURCE_BUNDLE_NAME = "message";


    private boolean isValidPlayerName(String playerName) {
        // プレイヤー名がnullまたは空文字列の場合は無効
        if (playerName == null || playerName.isEmpty()) {
            return false;
        }
        // プレイヤー名が存在するかチェック
        Player player = Bukkit.getPlayerExact(playerName);
        return player != null && player.isValid();
    }


    //ユーザーの言語を取得するメソッド
    private Locale getLanguage(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;


            return player.locale();
        } else {
            // Player以外の場合はデフォルト言語を返す
            return Locale.ENGLISH;
        }
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //コマンドの実行時に実行される処理

        try {


            ResourceBundle bundle;
            Locale language = getLanguage(sender); //ユーザーの言語を取得するメソッドの呼び出し

            bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, language);


            FileConfiguration config = tpPlayer.getConfig();


            //エラーメッセージの取得
            String onlyPlayersCommand = bundle.getString("ONLY_PLAYERS_COMMAND");
            String invalidPlayerName = bundle.getString("INVALID_PLAYER_NAME");
            String noPermission = bundle.getString("NO_PERMISSION");
            String usage = bundle.getString("USAGE");
            String offline = bundle.getString("OFFLINE");
            String teleported = bundle.getString("TELEPORTED");


            //コマンドを実行したのがプレイヤーでない場合は処理を終了する
            if (!(sender instanceof Player)) {
                sender.sendMessage(onlyPlayersCommand);
                return true;
            }

            Player player = (Player) sender; //コマンドを実行したプレイヤーを取得

            //引数が足りない場合は使い方を表示する
            if (args.length < 1) {
                sender.sendMessage(usage);
                return true;
            }

            String targetPlayerName = args[0];
            Player targetPlayer = player.getServer().getPlayer(targetPlayerName); //テレポート先のプレイヤーを取得

            //権限をチェック
            if(config.getBoolean("tpplayer.tp")) {
                if (!sender.hasPermission("tpplayer.tp")) {
                    sender.sendMessage(noPermission);
                    return true;
                }
            }



            //プレイヤー名が正規表現かチェック
            if (!isValidPlayerName(targetPlayerName)) {
                sender.sendMessage(invalidPlayerName);
                return true;
            }




            //テレポート先のプレイヤーがオンラインかチェック
            if (targetPlayer == null) {
                sender.sendMessage(offline);
                return true;
            }

            //テレポートを実行
            player.teleport(targetPlayer);
            String teleportedMessage = String.format(teleported, targetPlayerName);

            sender.sendMessage(teleportedMessage);
            return true;
        } catch (Exception e) {
            // 例外が発生した場合の処理
            sender.sendMessage("An error occurred while executing the command: " + e.getMessage());
            e.printStackTrace(); // エラーのスタックトレースを出力
            return false;
        }
    }
}

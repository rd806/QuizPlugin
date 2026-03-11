package org.rd806.quizplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.rd806.quizplugin.QuizPlugin;
import org.rd806.quizplugin.quiz.Quiz;

public class QuizCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {

        Player player;

        // 发送Quiz
        if (args[0].equals("send")) {
            if (args.length == 2) {
                int id = Integer.parseInt(args[1]);
                QuizPlugin.main.quizConfig.sendSpecificQuiz(id);
                return true;
            }
            QuizPlugin.main.quizConfig.sendRandomQuiz();
            return true;
        }

        // 打开Quiz界面
        if (args[0].equals("open")) {
            if (sender instanceof Player) {
                player = (Player) sender;
                if (!player.hasPermission("quizplugin.open")) {
                    sender.sendMessage("§cYou do not have permission \"quizplugin.open\" to use this command.");
                    return false;
                }
                Inventory quizMenu = QuizPlugin.main.quizGui.createQuizMenu(player);
                player.openInventory(quizMenu);
                return true;
            }
        }

        // 查找操作
        if (args[0].equals("show")) {
            // 权限检查
            if (sender instanceof Player) {
                player = (Player) sender;
                if (!player.hasPermission("quizplugin.show")) {
                    sender.sendMessage("§cYou do not have permission \"quizplugin.show\" to use this command.");
                    return false;
                }
            }
            // 获取题目数量
            if (args[1].equals("num")) {
                sender.sendMessage("The total number of the Quiz is " + QuizPlugin.main.quizConfig.getMaxNum());
                return true;
            }
            // 展示题目信息
            if (args[1].equals("info")) {
                // 获取当前Quiz信息
                if (args.length == 2) {
                    QuizPlugin.main.quizConfig.getQuizInfo(sender, QuizPlugin.main.quiz);
                    return true;
                }
                // 获取特定 ID 的 Quiz 信息
                int num = Integer.parseInt(args[2]);
                if (num > QuizPlugin.main.quizConfig.getMaxNum()) {
                    QuizPlugin.logger.warning("The chosen id is out of bounds or NOT FOUND!");
                    return false;
                }
                Quiz temp = QuizPlugin.main.quizConfig.getQuizById(num);
                QuizPlugin.main.quizConfig.getQuizInfo(sender, temp);
                return true;
            }
        }

        // 重载Quiz
        if (args[0].equals("reload")) {
            QuizPlugin.main.quizConfig.reload();
            sender.sendMessage("Reload successful!");
            return true;
        }

        return false;
    }
}

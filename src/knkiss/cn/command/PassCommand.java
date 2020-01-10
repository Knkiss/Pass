package knkiss.cn.command;

import knkiss.cn.Pass;
import knkiss.cn.player.PlayerInfoList;
import knkiss.cn.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PassCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0)return false;
        if(args[0].equalsIgnoreCase("me")){
            //pass me
            if(!(sender instanceof Player)) return false;
            Player p = (Player)sender;
            Pass.infoList.showTask(p);


        }else if(args[0].equalsIgnoreCase("finish")){
            //pass finish
            if(!(sender instanceof Player)) return false;
            Player p = (Player)sender;
            if(Pass.taskList.canFinish(p)){
                Pass.taskList.Finish(p);
            }
        }else if(args[0].equalsIgnoreCase("reward")){
            //pass finish
            if(!(sender instanceof Player)) return false;
            Pass.infoList.showReward((Player)sender);
        }else if(args[0].equalsIgnoreCase("check")){
            if(!sender.hasPermission("Pass.check")){
                sender.sendMessage("你没有[Pass.check]权限");
                return true;
            }
            //pass check [task/player]
            if (args.length == 1){
                Pass.infoList.check(sender);
                Pass.taskList.check(sender);
            }else{
                if(args[1].equalsIgnoreCase("task")){
                    Pass.taskList.check(sender);
                }else if(args[1].equalsIgnoreCase("player")){
                    Pass.infoList.check(sender);
                }else {
                    sender.sendMessage("/pass check [task/player]");
                }
            }
        }else if(args[0].equalsIgnoreCase("set")){
            if(!sender.hasPermission("Pass.set")){
                sender.sendMessage("你没有[Pass.set]权限");
                return true;
            }
            //pass set <name> <number>
            if (args.length == 3){
                if(Util.canParseInt(args[2])){
                    int level = Integer.parseInt(args[2]);
                    if(!Pass.infoList.setPlayerLevel(args[1],level)){
                        sender.sendMessage("不存在 "+ args[1] +" 玩家");
                    }else{
                        if (level >= PlayerInfoList.maxLevel) level = 1;
                        sender.sendMessage("已将 "+ args[1] +" 玩家等级设置为 "+level);
                    }
                }else{
                    sender.sendMessage("/pass set <name> <number> number必须为纯数字");
                }
            }else{
                sender.sendMessage("/pass set <name> <number>");
            }
        }else if(args[0].equalsIgnoreCase("buy")){
            if(!sender.hasPermission("Pass.buy")){
                sender.sendMessage("你没有[Pass.buy]权限");
                return true;
            }
            if(!(sender instanceof Player))return false;
            Player p = (Player) sender;
            //pass buy <number>
            if (args.length == 2) {
                if (Util.canParseInt(args[1])) {
                    int i = Pass.infoList.canBS(p, Integer.parseInt(args[1]));
                    if (i == 0)
                        Pass.infoList.buyLevel(p, Integer.parseInt(args[1]));
                    if (i == 1) sender.sendMessage("购买后等级超出上限");
                    if (i == 2) sender.sendMessage("购买任务中含 不可完成/有错误 的任务");
                }
                if (!Util.canParseInt(args[1])) sender.sendMessage("/pass buy <number> number必须为整数");
            }  if (args.length != 2) sender.sendMessage("/pass buy <number>");
        }else if(args[0].equalsIgnoreCase("skip")) {
            if(!sender.hasPermission("Pass.skip")){
                sender.sendMessage("你没有[Pass.skip]权限");
                return true;
            }
            if(!(sender instanceof Player))return false;
            Player p = (Player) sender;
            //pass skip <number>
            if (args.length == 2) {
                if (Util.canParseInt(args[1])) {
                    int i = Pass.infoList.canBS(p, Integer.parseInt(args[1]));
                    if (i == 0)
                        Pass.infoList.skipLevel(p, Integer.parseInt(args[1]));
                    if (i == 1) sender.sendMessage("跳过后等级超出上限");
                    if (i == 2) sender.sendMessage("跳过任务中含 不可完成/有错误 的任务");
                }
                if (!Util.canParseInt(args[1])) sender.sendMessage("/pass skip <number> number必须为整数");
            } if (args.length != 2) sender.sendMessage("/pass skip <number>");
        }else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
            //pass help
            sender.sendMessage("---------------------Pass 通行证---------------------------");
            sender.sendMessage("/pass help - 查看帮助");
            sender.sendMessage("/pass me - 查看自己的任务表");
            sender.sendMessage("/pass finish - 完成当前任务");
            sender.sendMessage("/pass reward - 查看自己的奖励箱");
            sender.sendMessage("/pass skip <number> - 跳过number个任务");
            sender.sendMessage("/pass buy <number> - 完成number个任务");
            sender.sendMessage("/pass check [task/player] - 查看当前内部信息[维护]");
            sender.sendMessage("/pass set <name> <level:1~maxlevel> - 设置玩家等级[待删除]");
        }
        return true;
    }
}

package knkiss.cn.Command;

import knkiss.cn.Pass;
import knkiss.cn.PlayerInfoList;
import knkiss.cn.util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class passCommand implements CommandExecutor {

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
            if(Pass.infoList.canFinish(p)){
                Pass.infoList.Finish(p);
            }
        }else if(args[0].equalsIgnoreCase("reward")){
            //pass finish
            if(!(sender instanceof Player)) return false;
            Pass.infoList.showReward((Player)sender);
        }else if(args[0].equalsIgnoreCase("check")){
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
            //pass set <name> <number>
            if (args.length == 3){
                //等待编辑
                if(util.canParseInt(args[2])){
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
        }else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
            //pass help/?
            sender.sendMessage("/pass help/? - 查看帮助");
            sender.sendMessage("/pass me - 查看自己的任务表");
            sender.sendMessage("/pass finish - 完成当前任务");
            sender.sendMessage("/pass check [task/player] - 查看当前内部信息");
            sender.sendMessage("/pass set <name> <level:1~maxlevel> - 设置玩家等级");
        }
        return true;
    }
}

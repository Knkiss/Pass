package knkiss.cn.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("这是个测试命令，无效果");
        //Pass.effect.Yuan_Foot((Player) commandSender);
        return true;
    }
}
package knkiss.cn.command;

import knkiss.cn.Pass;
import knkiss.cn.effect.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Pass.particle.Yuan_Foot((Player) commandSender);
        return true;
    }
}

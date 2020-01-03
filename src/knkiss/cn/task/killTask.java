package knkiss.cn.task;

import org.bukkit.entity.Player;

public class killTask extends task {

    public killTask(String path) {
        super(path);
    }

    @Override
    public boolean canFinish(Player p) {
        return false;
    }

    @Override
    public void Finish(Player p) {

    }
}

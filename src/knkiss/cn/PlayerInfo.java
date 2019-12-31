package knkiss.cn;

public class PlayerInfo {
    String name;
    int level = 0;

    PlayerInfo(String name){
        this.name = name;
        this.level = Pass.config.getInt("player."+name+".level");
    }
}

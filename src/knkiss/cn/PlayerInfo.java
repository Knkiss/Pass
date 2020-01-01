package knkiss.cn;

public class PlayerInfo {
    String name;
    int level = 0;
    String path;

    PlayerInfo(String name){
        this.path = "player."+name;
        this.name = name;
        this.level = Pass.config.getInt(path+".level");
    }

    public void updateConfig(){
        Pass.config.set(path+".level",level);
    }
}

package knkiss.cn;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskList {

    private static HashMap<String,Task> list = new HashMap<>();

    TaskList(){

        Set<String> keys= Pass.config.getKeys(true);
        String pattern = "(task..*).level";
        Pattern r = Pattern.compile(pattern);

        keys.forEach(path->{
            Matcher m = r.matcher(path);
            if(m.find()){
                path = m.group(1);
                int num = Pass.config.getInt(path+".level");
                list.put(String.valueOf(num),new Task(path));
            }
        });
    }

    public Task getTask(int level){
        return list.get(String.valueOf(level));
    }


}

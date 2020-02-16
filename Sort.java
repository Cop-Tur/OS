package OS;

import java.util.Comparator;

public class Sort implements Comparator<Process> {

    @Override
    public int compare(Process process, Process t1) {
        return t1.getPriority()-process.getPriority();
    }
}

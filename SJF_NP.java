package ur_os;

import java.util.Comparator;

public class SJF_NP extends Scheduler {

    SJF_NP(OS os) {
        super(os);
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (cpuEmpty && !processes.isEmpty()) {

            Process shortestJob = processes.stream()
                    .min(Comparator.comparingInt(Process::getRemainingTimeInCurrentBurst))
                    .orElse(null);
            processes.remove(shortestJob);

            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortestJob);
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {

    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {

    }
}
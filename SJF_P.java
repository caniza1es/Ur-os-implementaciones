package ur_os;

import java.util.Comparator;

public class SJF_P extends Scheduler {

    SJF_P(OS os) {
        super(os);
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (!processes.isEmpty()) {
            Process shortestJob = processes.stream()
                    .min(Comparator.comparingInt(p -> p.getRemainingTimeInCurrentBurst()))
                    .orElse(null);
            if (shortestJob != null) {
                if (cpuEmpty) {

                    processes.remove(shortestJob);
                    os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortestJob);
                } else {
                    Process currentProcess = os.getProcessInCPU();
                    if (currentProcess != null && shortestJob.getRemainingTimeInCurrentBurst() < currentProcess.getRemainingTimeInCurrentBurst()) {

                        processes.remove(shortestJob);
                        os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, currentProcess); // Preempt the current process
                        os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortestJob); // Schedule the new shortest job
                    }
                }
            }
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {

        if (!processes.isEmpty()) {
            Process shortestJob = processes.stream()
                    .min(Comparator.comparingInt(p -> p.getRemainingTimeInCurrentBurst()))
                    .orElse(null);
            Process currentProcess = os.getProcessInCPU();
            if (shortestJob != null && currentProcess != null &&
                    shortestJob.getRemainingTimeInCurrentBurst() < currentProcess.getRemainingTimeInCurrentBurst()) {

                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, currentProcess);
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortestJob);
            }
        }
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {

        if (!processes.isEmpty()) {
            Process shortestJob = processes.stream()
                    .min(Comparator.comparingInt(p -> p.getRemainingTimeInCurrentBurst()))
                    .orElse(null);
            Process currentProcess = os.getProcessInCPU();
            if (shortestJob != null && currentProcess != null &&
                    shortestJob.getRemainingTimeInCurrentBurst() < currentProcess.getRemainingTimeInCurrentBurst()) {
                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, currentProcess);
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortestJob);
            }
        }
    }
}

package som_processing.filters;

import som_processing.data_model.SystemCall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class filterTimeoutArgSyscalls2 {

    public void filterTOSysOnEU(List<SystemCall> EU) {
        Map<String, Integer> sysarg = new HashMap<String, Integer>();
        for (SystemCall syscall : EU) {
            if (filterTOSyscalls(syscall.getName())) {
                if (sysarg.containsKey(syscall.getName())) {
                    int tmp = sysarg.get(syscall.getName());
                    sysarg.put(syscall.getName(), tmp + 1);
                } else {
                    sysarg.put(syscall.getName(), 1);

                }
            }
        }

        for (Map.Entry<String, Integer> entry : sysarg.entrySet()) {
            System.out.println("##### timeout argment syscall count: " + entry.getKey() + " = " + entry.getValue() + "\n");
        }
    }

    private boolean filterTOSyscalls(String line) {
        if (line.contains("__newselect") || line.contains("_alarm")
                || line.contains("_clock_nanosleep") || line.contains("_epoll_pwait")
                || line.contains("_epoll_wait") || line.contains("_futex")
                || line.contains("_io_getevents") || line.contains("_mq_timedreceive")
                || line.contains("_mq_timedsend") || line.contains("_nanosleep")
                || line.contains("_poll") || line.contains("_ppoll")
                || line.contains("_pselect6") || line.contains("_recvmmsg")
                || line.contains("_rt_sigtimedwait") || line.contains("_select")
                || line.contains("_semtimedop") || line.contains("_timerfd_settime")
                || line.contains("_timer_settime")) {
            return true;
        }
        return false;
    }
}

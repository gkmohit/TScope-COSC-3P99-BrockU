package som_processing.filters;

import som_processing.data_model.SystemCall;

import java.util.List;

public class filterTimeoutSyscallsAfterAEU {

    boolean filterTOSysOnEU(List<SystemCall> EU) {
        for (SystemCall syscall : EU) {
            if (fileterSyscallsHasTOArgs(syscall.getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean fileterSyscallsHasTOArgs(String line) {
        if (line.contains("_newselect") ||
                line.contains("_alarm") ||
                line.contains("_clock_nanosleep") ||
                line.contains("_epoll_pwait") ||
                line.contains("_epoll_wait") ||
                line.contains("_futex") ||
                line.contains("_io_getevents") ||
                line.contains("_mq_timedreceive") ||
                line.contains("_mq_timedsend") ||
                line.contains("_nanosleep") ||
                line.contains("_poll") ||
                line.contains("_ppoll") ||
                line.contains("_select") ||
                line.contains("_recvmmsg") ||
                line.contains("_rt_sigtimedwait") ||
                line.contains("_pselect") ||
                line.contains("_semtimedop") ||
                line.contains("_timerfd_settime") ||
                line.contains("_timer_settime"))
            return true;
        return false;
    }
}


package som_processing;

import som_processing.data_model.SystemCall;
import som_processing.filters.filterTimeoutSyscalls;
import som_processing.processors_2.sysprocessing2;
import som_processing.views.FuncLocPreProcessingNoFilterNew;

import java.io.IOException;
import java.util.List;

public class app3 {
    public static void main(String[] args) throws IOException {
        long gap = Long.parseLong(args[3]);
        double start_t = System.currentTimeMillis();

        FuncLocPreProcessingNoFilterNew prepro = new FuncLocPreProcessingNoFilterNew();
        List<List<SystemCall>> syscall = prepro.startPreprocessing(args[0], args[1], args[2], gap);
        filterTimeoutSyscalls filterSyscalls2 = new filterTimeoutSyscalls();
        List<List<SystemCall>> syscallLists = filterSyscalls2.filterTOSysOnEU(syscall);

        System.out.println("\n" + " Function processing Done");

        sysprocessing2 vector = new sysprocessing2();
        vector.doAnalysis(syscallLists, args[0], args[1], null, gap, args[4]);

        double end_t = System.currentTimeMillis();
        System.out.println("The total elapsed time: " + (end_t - start_t) / 1000L + " seconds.");

        System.out.println("\n" + " Analysis Done");

    }

}

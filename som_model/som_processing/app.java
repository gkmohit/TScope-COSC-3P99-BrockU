package som_processing;

import som_processing.data_model.SystemCall;
import som_processing.processors.FuncLocPreProcessing;
import som_processing.processors.processing;

import java.io.IOException;
import java.util.List;

public class app {
    public static void main(String[] args) throws IOException {
        FuncLocPreProcessing PreProcessing = new FuncLocPreProcessing();
        List<List<List<SystemCall>>> syscall = PreProcessing.startPreprocessing("java", "hadoop", "/home/jhe16/Downloads/PerfScopeAEU/raw-syscall/syscall_11252.log");
        System.out.println("\n" + " Function processing Done");
        processing vector = new processing();
        vector.doAnalysis(syscall);

        System.out.println("\n" + " Analysis Done");
    }

}

package som_processing;

import som_processing.data_model.SystemCall;
import som_processing.processors_2.FunLocPreProcessing2;
import som_processing.processors_2.processing2;

import java.io.IOException;
import java.util.List;


public class app2 {
    public static void main(String[] args) throws IOException {
        FunLocPreProcessing2 PreProcessing = new FunLocPreProcessing2();
        List<SystemCall> syscall = PreProcessing.startPreprocessing("java", "phoenix", "./SampleLog.txt");
        processing2 vector = new processing2();
        vector.doAnalysis(syscall);
    }

}

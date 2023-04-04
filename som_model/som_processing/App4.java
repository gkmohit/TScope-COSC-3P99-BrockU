package som_processing;

import som_processing.data_extractors.ExtractSystemCalls;
import som_processing.data_model.SystemCall;
import som_processing.processors_2.processing2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class App4 {
    public static void main(String[] args) {
        String fileName = "SampleLog.txt";
        ExtractSystemCalls e = new ExtractSystemCalls();
        try {
            List<SystemCall> syscall= e.extractor(fileName);
            processing2 vector = new processing2();
            vector.doAnalysis(syscall);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}

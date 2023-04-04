package som_processing.data_extractors;

import som_processing.data_model.SystemCall;


import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ExtractSystemCalls {

    public static Logger logger = Logger.getLogger(ExtractSystemCalls.class
            .getName());
    public ExtractSystemCalls(){
    }

    public List<SystemCall> extractor(String fileName) throws FileNotFoundException, ParseException {
        logger.info("Starting Extraction");
        List<SystemCall> returnList = new ArrayList<>();
        File file = new File(fileName);
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()){
            //Setting up variables for system call
            long timeStamp = -1;
            int tid = -1;
            String name;
            //Variable to hold each file line
            String line = reader.nextLine();
            name = line.split("\t")[3];
            line = line.replace("\t", " ");
            String[] split = line.split(" ");

            //Extracting time stamp
            if(!split[0].equals("Timestamp")) {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                        .parse("2023-03-27 " + split[0]);
                timeStamp = date.getTime();
            }
            for(String s : split){
                //Extracting TID
                if( s.startsWith("tid")){
                    s = s.replace(',', ' ');
                    s.trim();
                    name = name + " " + s;
                    try {
                        tid = Integer.parseInt(s.split("=")[1].trim());
                    } catch (NumberFormatException numberFormatException){
                        logger.severe("Number format: Line: " + line + " Split Value: " + s);
                    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException){
                        logger.severe("Out of bound: Line: " + line + " Split Value: " + s);
                    }

                }
            }
            //TODO only add the required system calls
            SystemCall systemCall = new SystemCall(timeStamp, name, tid);
            returnList.add(systemCall);
        }
        logger.info("Extracted " + returnList.size() + " System Calls from the file: " + fileName);
        return returnList;
    }
}

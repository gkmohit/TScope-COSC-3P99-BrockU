package som_processing;

import som_processing.Utility.Constants;
import som_processing.data_model.SystemCall;
import som_processing.processors.FuncLocPreProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class simpliedpre {
    public static Logger logger = Logger.getLogger(FuncLocPreProcessing.class
            .getName());

    public List<List<SystemCall>> outputLog(List<SystemCall> systemCallList) throws IOException {
        if (systemCallList == null || systemCallList.isEmpty()) {
            return null;
        }

        long maxtime = systemCallList.get(0).getTimeStamp();
        long mintime = systemCallList.get(0).getTimeStamp();

        for (int i = 0; i < systemCallList.size(); i++) {
            if (maxtime < systemCallList.get(i).getTimeStamp()) {
                maxtime = systemCallList.get(i).getTimeStamp();
            }
            if (mintime > systemCallList.get(i).getTimeStamp()) {
                mintime = systemCallList.get(i).getTimeStamp();
            }
        }

        System.out.println("Number of syscalls in systemCallList:" + systemCallList.size());
        System.out.println("maxtime:" + maxtime);
        System.out.println("mintime:" + mintime);
        int totalinterval = (int) ((maxtime - mintime) / 1000000000);
        System.out.println("total:" + totalinterval);
        // split by timestamp
        List<List<SystemCall>> syswhole = new ArrayList<List<SystemCall>>();

        int startPosition = 0;
        int endPosition = 0;
        int thretime = 0;
        for (int i = 0; i < systemCallList.size(); i++) {
            endPosition = 0;
            long diff = systemCallList.get(i).getTimeStamp() - mintime;
            int validx = (int) (diff / 1000000000);
            if (i == 0) {
                thretime = validx;
            }
            if (thretime != validx) {
                endPosition = i;
                //          System.out.println("startPosition:" +  startPosition);
                System.out.println("endPosition:" + endPosition + "\n");
            }

            if (startPosition != endPosition) {
                List<SystemCall> ExecutionUnitlist = systemCallList.subList(
                        startPosition, endPosition);
                syswhole.add(ExecutionUnitlist);
                startPosition = endPosition;
                System.out.println("startPosition:" + startPosition);
            }

            if (i == systemCallList.size() - 1) {
                List<SystemCall> ExecutionUnitlist = systemCallList.subList(
                        startPosition, i + 1);
                syswhole.add(ExecutionUnitlist);
            }

            thretime = validx;
        }
        System.out.println("spliting time interval done");
        System.out.println("Number of units:" + syswhole.size());

        FileWriter f = null;
        BufferedWriter out = null;
        f = new FileWriter(Constants.SORT_UNIT_TXT);
        out = new BufferedWriter(f);
        int cnt = 0;
        int cnt2 = 0;
        for (int i = 0; i < syswhole.size(); i++) {
            out.write(syswhole.get(i).get(0).getTimeStamp() + ":" + "\n");
            cnt2 = cnt2 + 1;
            //  System.out.println("Number of units in syswhole:" + cnt2);
            for (int k = 0; k < syswhole.get(i).size(); k++) {
                cnt = cnt + 1;
                out.write(syswhole.get(i).get(k).getName() + "," + syswhole.get(i).get(k).getTimeStamp() + ","
                        + syswhole.get(i).get(k).getTid() + "\n");
                out.write("\n");
            }
        }
        out.flush();
        out.close();

        System.out.println("Number of syscalls in syswhole:" + cnt);
        System.out.println("Number of units in syswhole:" + cnt2);

        return syswhole;
    }

    public int getPercentile(List<Integer> listToCheck, double pValue) {
        int index = (int) (listToCheck.size() * pValue);
        Collections.sort(listToCheck);
        int cIndex = 0;
        int ret = 0;
        for (int item : listToCheck) {
            cIndex = cIndex + 1;
            if (cIndex == index) {
                ret = item;
                break;
            }
        }
        return ret;
    }

    public double calcSTD(double avg, List<Long> cList) {
        double cTotal = 0;
        for (long i : cList) {
            double cDiff = i - avg;
            cDiff = cDiff * cDiff;
            cTotal += cDiff;
        }
        cTotal = (1.0 * cTotal) / cList.size();
        return Math.sqrt(cTotal);
    }

    public long getMillisFromGMT(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            logger.severe("parse GMT timestamp to produce millis failed");
            return -1;
        }
    }

    // Parses the current line and puts the results into the system call data
    // structure format
    public SystemCall getSystemCall(String line) {
        if (line.equals("-1"))
            return new SystemCall(-1, "CONTEXT_SWITCH", -1);
        String timeString = line.split("\\]")[0].split("\\[")[1];
        String[] contextsRaw = line.split("\\{")[2].split("\\}")[0].split(",");
        String TID = "";
        for (String item : contextsRaw)
            if (item.contains("tid ="))
                TID = item.split("tid = ")[1].replaceAll("\\s", "");
        String timedouble = Long.toString(getMillisFromGMT(timeString
                .substring(0, timeString.length() - 6)))
                + "."
                + (timeString.substring(timeString.length() - 6));
        double timeStamp = Double.parseDouble(timedouble);
        long timeStampLong = (long) (timeStamp * 1000000);
        String[] systemCallTemp = line.split("\\{")[0].split(" ");
        String systemCall = systemCallTemp[systemCallTemp.length - 1]
                .replaceAll(":", "");
        return new SystemCall(timeStampLong, systemCall, Integer.parseInt(TID));
    }

    // Segments the input file based on process name while inserting context
    // switch markers into the list.
    // Hands resulting list to the outputLog function to be further segmented by
    // TID and large time gaps.
    public List<SystemCall> getSyscallList(String fileName, String procNames,
                                           String language) throws IOException {

        InputStream instream = null;
        BufferedReader reader = null;
        Map<String, Integer> pMap = new HashMap<String, Integer>();
        if (language.equalsIgnoreCase("c") || language.equalsIgnoreCase("c++")) {
            if (procNames.contains(",")) {
                String[] pTemp = procNames.split(",");
                for (String pName : pTemp)
                    pMap.put(pName, 1);
            } else
                pMap.put(procNames, 1);
        } else if (language.equalsIgnoreCase("java")) {
            pMap.put("java", 1);
        }
        SystemCall previousSyscall = new SystemCall();
        boolean previousFlag = false;
        List<SystemCall> syscallList = new ArrayList<SystemCall>();
        List<SystemCall> syscallorigin = new ArrayList<SystemCall>();
//		Map<Integer, List<SystemCall>> scListMap = new HashMap<Integer, List<SystemCall>>();
        int pTID = -1;
        String line = "";
        try {
            instream = new FileInputStream(fileName);//for testing....
            logger.info("load " + fileName + " from storage successfully.");
            reader = new BufferedReader(new InputStreamReader(instream));
            while ((line = reader.readLine()) != null) {
                boolean use = false;
                Iterator<Entry<String, Integer>> pIt = pMap.entrySet()
                        .iterator();
                Entry<String, Integer> pEntry = null;
                while (pIt.hasNext()) {
                    pEntry = pIt.next();
                    String key = pEntry.getKey();
                    if (line.contains(key)) {
                        use = true;
                        break;
                    }
                }

                // if not sc.procName in pMap:
                if (!use) {
                    continue;
                }

                SystemCall sc = getSystemCall(line);
                syscallorigin.add(sc);
            }

        } catch (Exception e) {
            logger.severe("Exception: " + e.toString());
        }
        // logger.info("scListMap size = " + scListMap.size());
        logger.info("Done creating list");
        try {
            for (SystemCall sc2 : syscallorigin) {
                if (sc2.name.equals("CONTEXT_SWITCH")) {
                    // syscallList.add(sc2);
                    continue;
                }
                if ((sc2.name.contains("exit_syscall") || sc2.name
                        .contains("syscall_exit")) && previousFlag) {
                    previousSyscall.exitValue = sc2.timeStamp;
                    syscallList.add(previousSyscall);
                } else {
                    previousSyscall = sc2;
                    previousFlag = true;
                }
            }
        } catch (Exception e) {
            logger.severe("Exception: " + e.toString());
            //		scListMap.clear();
            return null;
        }

        FileWriter f = null;
        BufferedWriter out = null;
        f = new FileWriter(Constants.SORT_SYS_TXT);
        out = new BufferedWriter(f);
        for (int i = 0; i < syscallList.size(); i++) {
            out.write(syscallList.get(i).getTimeStamp() + ",");
            out.write(syscallList.get(i).getName() + ",");
            out.write(syscallList.get(i).getTid() + ",");
            out.write(syscallList.get(i).getExitValue() + ",");
            out.write("\n");
        }
        out.flush();
        if (f != null)
            f.close();
        if (out != null)
            out.close();

        return syscallList;
    }

    /***
     *  Entry point, parses the arguments and calls the functions to segment the
     *  input system call log.
     * "java" "hadoop,hdfs", "/home/.../lttngtrace.log"
     * @param language
     * @param procNames
     * @param syscallLog
     * @return
     * @throws IOException
     */

    public List<List<SystemCall>> startPreprocessing(String language,
                                                     String procNames, String syscallLog) throws IOException {
        logger.info("Segmenting log " + syscallLog);
        double start_t = System.currentTimeMillis();
        List<SystemCall> systemCallList = getSyscallList(syscallLog, procNames,
                language);
        logger.info("The Syscall List size = " + systemCallList.size());
        double end_t = System.currentTimeMillis();
        double elapsed = (end_t - start_t) / 1000L;
        logger.info("Total time: " + elapsed);
        List<List<SystemCall>> syscallLists = outputLog(systemCallList);
        return syscallLists;
    }

}

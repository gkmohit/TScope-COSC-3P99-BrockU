
package som_processing.data_model;

import java.sql.Timestamp;

//Representation of a system call

/***
 * A data model class to represent System Call.
 */
public class SystemCall {

    public long timeStamp;
    public String name;
    public int tid;
    public long exitValue;

    public SystemCall() {

    }

    public SystemCall(long timeStamp, String name, int tid) {
        this.timeStamp = timeStamp;
        this.name = name;
        this.tid = tid;
        this.exitValue = Long.MAX_VALUE;


    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public long getExitValue() {
        return exitValue;
    }

    public void setExitValue(long exitValue) {
        this.exitValue = exitValue;
    }

    public String printSyscall() {
        return timeStamp + " " + name + " " + tid + " " + exitValue + "\n";
    }

}

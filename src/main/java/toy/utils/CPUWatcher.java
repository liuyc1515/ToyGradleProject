package toy.utils;

import java.lang.management.ManagementFactory;
import java.lang.Thread;
import java.util.*;
import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

public class CPUWatcher implements Watcher {

    public Map<String, Object>  getUsage() {
        Map<String, Object> usageInfoBatch = new TreeMap<String, Object>();
        usageInfoBatch.put("OSMXBean", getUsageOSMXBean());
        usageInfoBatch.put("OSHI", getUsageOSHI());
        return usageInfoBatch;
    }

    private Map<String, Object> getUsageOSMXBean() {
        OperatingSystemMXBean managerOSMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Double cpuLoad = managerOSMXBean.getSystemCpuLoad();
        Map<String, Object> usageInfo = new TreeMap<String, Object>();
        usageInfo.put("using rate", cpuLoad);
        return usageInfo;
    }

    private Map<String, Object> getUsageOSHI() {
        SystemInfo systemInfoOSHI = new SystemInfo();
        CentralProcessor processorOSHI = systemInfoOSHI.getHardware().getProcessor();
        long[] prevTicks = processorOSHI.getSystemCpuLoadTicks();
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {

        }
	    long[] ticks = processorOSHI.getSystemCpuLoadTicks();
	    long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
	    long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
	    long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
	    long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
	    long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
	    long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

        Map<String, Object> usageInfo = new TreeMap<String, Object>();
        Double usageRate = new Double(0.0);
        if (totalCpu != 0) {
            usageRate = 1.0 - ((double)idle / totalCpu);
        }
        usageInfo.put("usage rate", usageRate);
        return usageInfo;
    }
}

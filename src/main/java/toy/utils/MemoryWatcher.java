package toy.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.lang.StringUtils;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

public class MemoryWatcher implements Watcher {

    public Map<String, Object> getUsage() {
        Map<String, Object> usageInfoBatch = new TreeMap<String, Object>();
        usageInfoBatch.put("OSMXBean", getUsageOSMXBean());
        usageInfoBatch.put("MemoryMXBean", getUsageMemoryMXBean());
        usageInfoBatch.put("OSHI", getUsageOSHI());
        usageInfoBatch.put("Linux", getUsageLinux());

        return usageInfoBatch;
    }

    private Map<String, Object> getUsageOSMXBean() {
        OperatingSystemMXBean managerOSMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Map<String, Object> usageInfo = new TreeMap<String, Object>();
        Long totalByte = managerOSMXBean.getTotalPhysicalMemorySize();
        Long freeByte = managerOSMXBean.getFreePhysicalMemorySize();
        usageInfo.put("total memory (kB)", totalByte / 1024);
        usageInfo.put("free memory (kB)", freeByte / 1024);

        return usageInfo;
    }

    private Map<String, Object> getUsageMemoryMXBean() {
        MemoryMXBean managerMemoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = managerMemoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = managerMemoryMXBean.getNonHeapMemoryUsage();
        Map<String, Object> usageInfo = new TreeMap<String, Object>();
        usageInfo.put("jvm heap using (kB)", heapMemoryUsage.getUsed());
        usageInfo.put("jvm heap total (kB)", heapMemoryUsage.getMax());
        usageInfo.put("jvm non heap using (kB)", nonHeapMemoryUsage.getUsed());
        usageInfo.put("jvm non heap total (kB)", nonHeapMemoryUsage.getMax());

        return usageInfo;
    }

    private Map<String, Object> getUsageOSHI() {
        SystemInfo systemInfoOSHI = new SystemInfo();
        GlobalMemory memoryOSHI = systemInfoOSHI.getHardware().getMemory();
        Long totalByte = memoryOSHI.getTotal();
        Long availableByte = memoryOSHI.getAvailable();
        Map<String, Object> usageInfo = new TreeMap<String, Object>();
        usageInfo.put("total memory (kB)", totalByte / 1024);
        usageInfo.put("available memory (kB)", availableByte / 1024);
        usageInfo.put("using rate", (double)(totalByte - availableByte) / totalByte);

        return usageInfo;
    }

    private Map<String, Object> getUsageLinux() {
        
        File memInfo = new File("/proc/meminfo");
        Long totalMemory = new Long(0);
        Long freeMemory = new Long(0);
        Long availableMemory = new Long(0);
        Double usingRate = new Double(0.0);
        Map<String, Object> usageInfo = new TreeMap<String, Object>();
        
        try {
            FileReader memInfoReader = new FileReader(memInfo);
            BufferedReader memInfoBuffer = new BufferedReader(memInfoReader);
            List<String> memInfoList = new ArrayList<String>();
            String memInfoLine;
            while ((memInfoLine = memInfoBuffer.readLine()) != null) {
                memInfoList.add(memInfoLine);
            }
            Map<String, String> memInfoMap = new TreeMap<String, String>();
            for (String Line : memInfoList) {
                String[] memInfoSplited = StringUtils.split(Line, ": \t");
                memInfoMap.put(memInfoSplited[0], memInfoSplited[1]);
            }
            totalMemory = Long.valueOf(memInfoMap.get("MemTotal"));
            freeMemory = Long.valueOf(memInfoMap.get("MemFree"));
            availableMemory = Long.valueOf(memInfoMap.get("MemAvailable"));
            usingRate = (double)(totalMemory - availableMemory) / totalMemory;
        }
        catch (IOException e) {

        }

        usageInfo.put("total memory (kB)", totalMemory);
        usageInfo.put("free memory (kB)", freeMemory);
        usageInfo.put("available memory (kB)", availableMemory);
        usageInfo.put("using rate", usingRate);

        return usageInfo;
    }
}

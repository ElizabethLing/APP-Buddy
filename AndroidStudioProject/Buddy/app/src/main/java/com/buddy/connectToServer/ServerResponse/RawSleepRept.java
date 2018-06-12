package com.buddy.connectToServer.ServerResponse;
// File Name: RawSleepRept.java
import java.util.*;
public class RawSleepRept {
    public int			id;
    public int			depth;
    // public long		startTime;
    // public long		endTime;
    public String		startTime;
    public String		endTime;
    public String		streamFrom;
    public String		streamTo;
    public List<PostureStat>	pstStats;
}

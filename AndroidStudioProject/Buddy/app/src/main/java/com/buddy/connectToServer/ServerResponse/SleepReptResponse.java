package com.buddy.connectToServer.ServerResponse;
// File Name: SleepReptResponse.java
import java.util.*;
public class SleepReptResponse {
    public int			result;
    public byte			pd; // 1: Daily, 2: Weekly, 3: Monthly
    public List<SleepRept>	sleeps;
}

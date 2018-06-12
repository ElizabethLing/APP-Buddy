package com.buddy.connectToServer.ServerResponse;
// File Name: EmotionReptResponse.java
import java.util.*;
public class EmotionReptResponse {
    public int			result;
    public byte			pd; // 1: Daily, 2: Weekly, 3: Monthly
    public List<EmotionRept>	emotions;
}

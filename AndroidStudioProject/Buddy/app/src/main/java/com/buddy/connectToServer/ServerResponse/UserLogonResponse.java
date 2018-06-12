package com.buddy.connectToServer.ServerResponse;
// File Name: UserLogonResponse.java

import java.util.List;

public class UserLogonResponse {
    public int			result;
    public String uid;
    public byte			cidCnt;
    public List<KidInfo> kids;
}

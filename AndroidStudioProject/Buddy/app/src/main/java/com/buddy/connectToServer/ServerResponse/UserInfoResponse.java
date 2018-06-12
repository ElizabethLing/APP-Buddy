package com.buddy.connectToServer.ServerResponse;
// File Name: UserInfoResponse.java
import java.util.*;
public class UserInfoResponse { // Request Code = 21, Implicit Table = 'User'
    public int			result;
    public String		gender;
    public String		mobile;
    public String		email;
    public String		birthDay;
    public String		address;
    public String		career;
    public String		sports;
    public String		music;
    public String		literature;
    public String		religion;
    public String		selfDesc;
    public List<KidInfo>	kids;
}

package com.buddy.manager;

import com.buddy.listener.OnReceivedMessage;

/**
 * Created by admin on 2016/7/18.
 */
public class MessageManager {



    private static volatile MessageManager instance;
    private static OnReceivedMessage onReceivedMessage;


    public static MessageManager getInstance() {
        if (instance == null) {
            synchronized (MessageManager.class) {
                if (instance == null) {
                    instance = new MessageManager();
                }
            }
        }
        return instance;
    }



    public static OnReceivedMessage getOnReceivedMessage() {
        return onReceivedMessage;
    }

    public static void setOnReceivedMessage(OnReceivedMessage onReceivedMessage) {
        MessageManager.onReceivedMessage = onReceivedMessage;
    }
}

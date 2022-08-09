package com.zeusz.bsc.app.network;


public class DaemonThread extends Thread {

    public DaemonThread(Runnable target) {
        super(target);
        setDaemon(true);
    }

}

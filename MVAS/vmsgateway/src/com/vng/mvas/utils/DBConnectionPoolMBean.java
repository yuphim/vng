package com.vng.mvas.utils;

public interface DBConnectionPoolMBean {

    public int getMaxPoolSize();

    public long getMaxQueueSize();

    public long getNumerActive();

    public long getMaxIdle();

    public long getNumerIdle();
}

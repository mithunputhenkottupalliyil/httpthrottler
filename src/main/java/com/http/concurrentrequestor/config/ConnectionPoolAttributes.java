package com.http.concurrentrequestor.config;

import lombok.Data;

@Data
public class ConnectionPoolAttributes {
    private long retryAfter;
    private int connectionTimeOut;
    private int connectionReqTimeout;
    private int maxTotal;
    private int defaultMaxPerRoute;
    private int defaultKeepAlive;
}

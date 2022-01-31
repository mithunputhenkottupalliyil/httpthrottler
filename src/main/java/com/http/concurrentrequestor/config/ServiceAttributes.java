package com.http.concurrentrequestor.config;

import lombok.Data;

@Data
public class ServiceAttributes {
    private String endPoint;
    private boolean secure;
}

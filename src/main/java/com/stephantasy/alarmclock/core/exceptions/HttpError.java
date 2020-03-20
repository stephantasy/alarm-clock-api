package com.stephantasy.alarmclock.core.exceptions;


import java.util.Collections;
import java.util.List;

public class HttpError {
    private Integer status;
    private String reason;
    private List<String> reasons;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public HttpError() {
    }

    public HttpError(Integer status, String reason) {
        this.status = status;
        this.reason = reason;
        reasons = Collections.emptyList();
    }

    public HttpError(Integer status, String reason, List<String> reasons) {
        this.status = status;
        this.reason = reason;
        this.reasons = reasons;
    }
}


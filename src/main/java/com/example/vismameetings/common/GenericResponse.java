package com.example.vismameetings.common;

public class GenericResponse {
    private Boolean success;
    private Object data;
    private String cause;

    public static GenericResponse success(Object data) {
        return new GenericResponse(true, data, null);
    }

    public static GenericResponse error(String cause) {
        return new GenericResponse(false, null, cause);
    }

    public GenericResponse(Boolean success, Object data, String cause) {
        this.success = success;
        this.data = data;
        this.cause = cause;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }

    public String getCause() {
        return cause;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}

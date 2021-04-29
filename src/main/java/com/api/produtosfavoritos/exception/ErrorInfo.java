package com.api.produtosfavoritos.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {

    public Integer httpStatus;
    public String statusName;
    public String message;
    public StringBuffer url;
    public Map<String, String> campos_invalidos;

    public ErrorInfo(Integer httpStatus, String statusName, StringBuffer url, Map<String, String> campos_invalidos) {
        this.httpStatus = httpStatus;
        this.statusName = statusName;
        this.url = url;
        this.campos_invalidos = campos_invalidos;
    }

    public ErrorInfo(Integer httpStatus, String statusName, String message, StringBuffer url) {
        this.httpStatus = httpStatus;
        this.statusName = statusName;
        this.message = message;
        this.url = url;
    }
}
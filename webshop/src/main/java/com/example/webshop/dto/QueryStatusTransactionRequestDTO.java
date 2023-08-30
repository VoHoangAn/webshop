package com.example.webshop.dto;

import org.intellij.lang.annotations.Language;

public class QueryStatusTransactionRequestDTO extends RequestDTO {
    private String signature;

    public QueryStatusTransactionRequestDTO(String signature) {
        this.signature = signature;
    }

    public QueryStatusTransactionRequestDTO(String partnerCode, String orderId, String requestId, String lang, String signature) {
        super(partnerCode, orderId, requestId, lang);
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

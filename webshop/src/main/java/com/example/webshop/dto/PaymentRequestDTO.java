package com.example.webshop.dto;

public class PaymentRequestDTO extends RequestDTO {
        private String orderInfo;

        private long amount;
        private String partnerName;
        private String subPartnerCode;
        private String requestType;
        private String redirectUrl;
        private String ipnUrl;
        private String storeId;
        private String extraData;
        private String partnerClientId;
        private Boolean autoCapture = true;
        private Long orderGroupId;
        private String signature;

        public PaymentRequestDTO(String partnerCode, String orderId, String requestId, String lang,
                                 String orderInfo, long amount, String partnerName, String subPartnerCode,
                                 String requestType, String redirectUrl, String ipnUrl, String storeId, String extraData, String partnerClientId, Boolean autoCapture, Long orderGroupId, String signature) {
            super(partnerCode, orderId, requestId, lang);
            this.orderInfo = orderInfo;
            this.amount = amount;
            this.partnerName = partnerName;
            this.subPartnerCode = subPartnerCode;
            this.requestType = requestType;
            this.redirectUrl = redirectUrl;
            this.ipnUrl = ipnUrl;
            this.storeId = storeId;
            this.extraData = extraData;
            this.partnerClientId = partnerClientId;
            this.autoCapture = autoCapture;
            this.orderGroupId = orderGroupId;
            this.signature = signature;
        }
}

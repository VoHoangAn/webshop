package com.example.webshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {
        private String partnerCode;
        private String requestId;
        private String orderId;
        private String lang = "vi";
        private long startTime;

        public RequestDTO() {
            this.startTime = System.currentTimeMillis();
        }

        public RequestDTO(String partnerCode, String orderId, String requestId, String lang) {
            this.partnerCode = partnerCode;
            this.orderId = orderId;
            this.requestId = requestId;
            this.lang = lang;
            this.startTime = System.currentTimeMillis();
        }
}

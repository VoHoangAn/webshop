package com.example.webshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
        protected long responseTime;
        protected String message;
        private String partnerCode;
        private String orderId;
        protected Integer resultCode;

        public ResponseDTO() {
            this.responseTime = System.currentTimeMillis();
        }
}

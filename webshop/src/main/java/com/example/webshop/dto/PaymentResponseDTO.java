package com.example.webshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentResponseDTO extends ResponseDTO {

        public PaymentResponseDTO(Integer resultCode, String message) {
            this.resultCode = resultCode;
            this.message = message;
        }

        private String requestId;

        private Long amount;

        private String payUrl;

        private String shortLink;

        private String deeplink;

        private String qrCodeUrl;

        private String deeplinkWebInApp;

        private Long transId;

        private String applink;

        private String partnerClientId;

        private String bindingUrl;

        private String deeplinkMiniApp;
}

package com.example.webshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class QueryStatusTransactionResponseDTO extends ResponseDTO {
        String requestId;
        String metaData;
        List<Object> refundTrans = new ArrayList<>();
        private Long amount;
        private String partnerUserId;
        private Long transId;
        private String extraData;
        private String payType;

        public QueryStatusTransactionResponseDTO(String requestId, String metaData, List<Object> refundTrans, Long amount, String partnerUserId, Long transId, String extraData, String payType) {
            this.requestId = requestId;
            this.metaData = metaData;
            this.refundTrans = refundTrans;
            this.amount = amount;
            this.partnerUserId = partnerUserId;
            this.transId = transId;
            this.extraData = extraData;
            this.payType = payType;
        }
}

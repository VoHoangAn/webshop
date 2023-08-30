package com.example.webshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpRequestDTO {
        private String method;
        private String endpoint;
        private String payload;
        private String contentType;

        public HttpRequestDTO(String method, String endpoint, String payload, String contentType) {
            this.method = method;
            this.endpoint = endpoint;
            this.payload = payload;
            this.contentType = contentType;
        }
}

package com.example.webshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import okhttp3.Headers;
@Getter
@Setter
@ToString
public class HttpResponseDTO {

        int status;
        String data;
        Headers headers;

        public HttpResponseDTO(int status, String data, Headers headers) {
            this.status = status;
            this.data = data;
            this.headers = headers;
        }
}

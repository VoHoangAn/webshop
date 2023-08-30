package com.example.webshop.service;

import com.example.webshop.cart.CartRepositoryImpl;
import com.example.webshop.dto.*;
import com.example.webshop.order.Order;
import com.example.webshop.order.OrderRepositoryImpl;
import com.example.webshop.orderDetail.OrderDetailRepositoryImpl;
import com.example.webshop.product.ProductRepositoryImpl;
import com.example.webshop.user.UserRepositoryImpl;
import com.example.webshop.util.Encoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class MomoPay {
    private final Logger logger = LoggerFactory.getLogger(MomoPay.class);
    private final String partnerCode = "MOMOBKUN20180529"; //"MOMO1JXE20211106";
    private final String accessKey = "klm05TvNBzhg7h7j"; //"e9uUo5EPsHtP26In";
    private final String secretKey = "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa"; //"L4Ls12l8tFnoz5vfvkTmuPcLpGvSTJiW";
    private final UserRepositoryImpl userRepository;
    private final OrderRepositoryImpl orderRepository;
    private final OrderDetailRepositoryImpl orderDetailRepository;
    private final CartRepositoryImpl cartRepository;
    private final ProductRepositoryImpl productRepository;
    OkHttpClient client = new OkHttpClient();

    public PaymentResponseDTO createOrder(String orderId, String requestId, String amount, String orderInfo,
                                          String returnURL, String notifyURL, String extraData,
                                          String requestType, Boolean autoCapture){
        try {

            String requestRawData = new StringBuilder()
                    .append("accessKey").append("=").append(accessKey).append("&")
                    .append("amount").append("=").append(amount).append("&")
                    .append("extraData").append("=").append(extraData).append("&")
                    .append("ipnUrl").append("=").append(notifyURL).append("&")
                    .append("orderId").append("=").append(orderId+"loda1").append("&")
                    .append("orderInfo").append("=").append(orderInfo).append("&")
                    .append("partnerCode").append("=").append(partnerCode).append("&")
                    .append("redirectUrl").append("=").append(returnURL).append("&")
                    .append("requestId").append("=").append(requestId).append("&")
                    .append("requestType").append("=").append(requestType)
                    .toString();

            String signRequest = Encoder.signHmacSHA256(requestRawData, secretKey);
            logger.info("[PaymentRequest] rawData: " + requestRawData + ", [Signature] -> " + signRequest);

            PaymentRequestDTO paymentRequest = new PaymentRequestDTO(partnerCode, orderId+"loda1", requestId, "vi", orderInfo, Long.valueOf(amount), "test MoMo", null, requestType,
                    returnURL, notifyURL, "test store ID", extraData, null, autoCapture, null, signRequest);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String payload = gson.toJson(paymentRequest, PaymentRequestDTO.class);

            String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
            HttpRequestDTO httpRequest = new HttpRequestDTO("POST", endpoint, payload, "application/json");

            Request request = createRequest(httpRequest);

            logger.debug("[HttpPostToMoMo] Endpoint:: " + httpRequest.getEndpoint() + ", RequestBody:: " + httpRequest.getPayload());

            Response result = client.newCall(request).execute();
            HttpResponseDTO response = new HttpResponseDTO(result.code(), result.body().string(), result.headers());

            logger.info("[HttpResponseFromMoMo] " + response.toString());

            PaymentResponseDTO captureMoMoResponse = gson.fromJson(response.getData(), PaymentResponseDTO.class);

//            getOrderStatus(Long.valueOf(orderId),order,requestId);
            return captureMoMoResponse;
        } catch (Exception exception) {
            logger.error("[CreateOrderMoMoProcess] "+ exception);
        }
        return null;
    }

    public CompletableFuture<Object> getOrderStatus(Long orderId, Order order, String requestId){
        try {
            String requestRawData = new StringBuilder()
                    .append("accessKey").append("=").append(accessKey).append("&")
                    .append("orderId").append("=").append(orderId+"loda1").append("&")
                    .append("partnerCode").append("=").append(partnerCode).append("&")
                    .append("requestId").append("=").append(requestId)
                    .toString();

            String signRequest = Encoder.signHmacSHA256(requestRawData, secretKey);
            logger.info("[QueryTransactionRequest] rawData: " + requestRawData + ", [Signature] -> " + signRequest);

            QueryStatusTransactionRequestDTO paymentRequest = new QueryStatusTransactionRequestDTO(partnerCode, orderId+"loda1", requestId, "vi", signRequest);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String payload = gson.toJson(paymentRequest, QueryStatusTransactionRequestDTO.class);

            String endpoint = "https://test-payment.momo.vn/v2/gateway/api/query";
            HttpRequestDTO httpRequest = new HttpRequestDTO("POST", endpoint, payload, "application/json");

            Request request = createRequest(httpRequest);

            logger.debug("[HttpPostToMoMo] Endpoint:: " + httpRequest.getEndpoint() + ", RequestBody:: " + httpRequest.getPayload());

            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

            /*return exec.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    Response result = null;
                    try {
                        result = client.newCall(request).execute();
                        HttpResponseDTO response = new HttpResponseDTO(result.code(), result.body().string(), result.headers());

                        logger.info("[HttpResponseFromMoMo] " + response.toString());
                        QueryStatusTransactionResponseDTO queryStatusTransactionResponse = gson.fromJson(response.getData(), QueryStatusTransactionResponseDTO.class);
                        if(queryStatusTransactionResponse.getResultCode()!=1000) {
                            if (queryStatusTransactionResponse.getResultCode() == 0) {
                                return;
                            }
                        } else {
                            getOrderStatus(orderId,requestId);
                        }
                        String responserawData = "requestId" + "=" + queryStatusTransactionResponse.getRequestId() +
                                "&" + "orderId" + "=" + queryStatusTransactionResponse.getOrderId() +
                                "&" + "message" + "=" + queryStatusTransactionResponse.getMessage() +
                                "&" + "resultCode" + "=" + queryStatusTransactionResponse.getResultCode();

                        logger.info("[QueryTransactionResponse] rawData: " + responserawData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
            }, 0, 10, TimeUnit.SECONDS);*/
            return sendQueryReqPerOneMin(request,orderId,order);

//            Response result = client.newCall(request).execute();
//            HttpResponseDTO response = new HttpResponseDTO(result.code(), result.body().string(), result.headers());
//
//            logger.info("[HttpResponseFromMoMo] " + response.toString());

//            QueryStatusTransactionResponseDTO queryStatusTransactionResponse = gson.fromJson(response.getData(), QueryStatusTransactionResponseDTO.class);
//            String responserawData = "requestId" + "=" + queryStatusTransactionResponse.getRequestId() +
//                    "&" + "orderId" + "=" + queryStatusTransactionResponse.getOrderId() +
//                    "&" + "message" + "=" + queryStatusTransactionResponse.getMessage() +
//                    "&" + "resultCode" + "=" + queryStatusTransactionResponse.getResultCode();
//
//            logger.info("[QueryTransactionResponse] rawData: " + responserawData);

//            return queryStatusTransactionResponse;
        } catch (Exception exception) {
            logger.error("[QueryTransactionProcess] "+ exception);
        }
        return null;
    }

    public CompletableFuture<Object> sendQueryReqPerOneMin(Request request, Long orderId, Order order) {
//        ExecutorService executor = Executors.newCachedThreadPool(r -> {
//            final Thread thread = new Thread(r);
//            thread.setDaemon(false); //change me
//            return thread;
//        });

        /*long timeDelay=0;
        for (int amountReqBeSentIn15Mins = 15; amountReqBeSentIn15Mins >0 ; amountReqBeSentIn15Mins--) {
            Executor delayed = CompletableFuture.delayedExecutor(timeDelay, TimeUnit.SECONDS);
            CompletableFuture.supplyAsync(() -> {
                Response result = null;
                try {
                    result = client.newCall(request).execute();
                    HttpResponseDTO response = new HttpResponseDTO(result.code(), result.body().string(), result.headers());

                    logger.info("[HttpResponseFromMoMo] " + response.toString());
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    QueryStatusTransactionResponseDTO queryStatusTransactionResponse = gson.fromJson(response.getData(), QueryStatusTransactionResponseDTO.class);
                    String responserawData = "requestId" + "=" + queryStatusTransactionResponse.getRequestId() +
                            "&" + "orderId" + "=" + queryStatusTransactionResponse.getOrderId() +
                            "&" + "message" + "=" + queryStatusTransactionResponse.getMessage() +
                            "&" + "resultCode" + "=" + queryStatusTransactionResponse.getResultCode();

                    logger.info("[QueryTransactionResponse] rawData: " + responserawData);
                    if (!queryStatusTransactionResponse.getResultCode().equals(1000)) {
                        if (queryStatusTransactionResponse.getResultCode() == 0) {
                            paymentSuccessHandler(orderId, order);
                            return 0;
                        }
//                        paymentFailureHandler(order);
                        return -1;
                    } else {
//                    sendQueryReqAfterOneMin(request,orderId,order);
                        return 1000;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, delayed);
            timeDelay+=10;
        }*/
        Executor delayed = CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS);
        return CompletableFuture.supplyAsync(() -> {
            try {
                Response result = client.newCall(request).execute();
                HttpResponseDTO response = new HttpResponseDTO(result.code(), result.body().string(), result.headers());

                logger.info("[HttpResponseFromMoMo] " + response.toString());
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                QueryStatusTransactionResponseDTO queryStatusTransactionResponse = gson.fromJson(response.getData(), QueryStatusTransactionResponseDTO.class);
                String responserawData = "requestId" + "=" + queryStatusTransactionResponse.getRequestId() +
                        "&" + "orderId" + "=" + queryStatusTransactionResponse.getOrderId() +
                        "&" + "message" + "=" + queryStatusTransactionResponse.getMessage() +
                        "&" + "resultCode" + "=" + queryStatusTransactionResponse.getResultCode();

                logger.info("[QueryTransactionResponse] rawData: " + responserawData);
                if (!queryStatusTransactionResponse.getResultCode().equals(1000)) {
                    if (queryStatusTransactionResponse.getResultCode() == 0) {
                        paymentSuccessHandler(orderId, order);
                        return 0;
                    }
                        paymentFailureHandler(order);
                    return -1;
                } else {
                    return sendQueryReqPerOneMin(request,orderId,order);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, delayed);
    }

    public void paymentSuccessHandler(Long orderId, Order order) {
        logger.info("paymentSuccessHandler"+orderId+order);
        try {
            userRepository.changeUserStateBackToUser(order.getUserId());
            orderRepository.changeOrderStatusToOrdered(orderId);
            productRepository.calculateQuantityByOrderItems(order.getOrderDetails());
            orderDetailRepository.saveAll(order.getOrderDetails());
            cartRepository.deleteAllByUserId(order.getUserId());
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void paymentFailureHandler(Order order) {
        logger.info("paymentFailHandler");
        userRepository.changeUserStateBackToUser(order.getUserId());
        orderRepository.deleteById(order.getId());
    }
//    public PaymentResponseDTO execute(PaymentRequestDTO request, String endPoint) throws Exception {
//        try {
//            Gson gson = new GsonBuilder().disableHtmlEscaping().create();;
//            String payload = gson.toJson(request, PaymentRequestDTO.class);
//
//            HttpResponseDTO response = sendToMoMo(endPoint, payload);
//
//            if (response.getStatus() != 200) {
//                throw new Exception("[PaymentResponse] [" + request.getOrderId() + "] -> Error API");
//            }
//
////            System.out.println("uweryei7rye8wyreow8: "+ response.getData());
//
//            PaymentResponseDTO captureMoMoResponse = gson.fromJson(response.getData(), PaymentResponseDTO.class);
//            String responserawData = "requestId" + "=" + captureMoMoResponse.getRequestId() +
//                    "&" + "orderId" + "=" + captureMoMoResponse.getOrderId() +
//                    "&" + "message" + "=" + captureMoMoResponse.getMessage() +
//                    "&" + "payUrl" + "=" + captureMoMoResponse.getPayUrl() +
//                    "&" + "resultCode" + "=" + captureMoMoResponse.getResultCode();
//
//            logger.info("[PaymentMoMoResponse] rawData: " + responserawData);
//
//            return captureMoMoResponse;
//
//        } catch (Exception exception) {
//            logger.error("[PaymentMoMoResponse] "+ exception);
//            throw new IllegalArgumentException("Invalid params capture MoMo Request");
//        }
//    }

//    public PaymentRequestDTO createPaymentCreationRequest(String orderId, String requestId, String amount, String orderInfo,
//                                                          String returnUrl, String notifyUrl, String extraData, String requestType, Boolean autoCapture) {
//
//        try {
//            String requestRawData = new StringBuilder()
//                    .append("accessKey").append("=").append(accessKey).append("&")
//                    .append("amount").append("=").append(amount).append("&")
//                    .append("extraData").append("=").append(extraData).append("&")
//                    .append("ipnUrl").append("=").append(notifyUrl).append("&")
//                    .append("orderId").append("=").append(orderId).append("&")
//                    .append("orderInfo").append("=").append(orderInfo).append("&")
//                    .append("partnerCode").append("=").append(partnerCode).append("&")
//                    .append("redirectUrl").append("=").append(returnUrl).append("&")
//                    .append("requestId").append("=").append(requestId).append("&")
//                    .append("requestType").append("=").append(requestType)
//                    .toString();
//
//            String signRequest = Encoder.signHmacSHA256(requestRawData, secretKey);
//            logger.info("[PaymentRequest] rawData: " + requestRawData + ", [Signature] -> " + signRequest);
//
//            return new PaymentRequestDTO(partnerCode, orderId, requestId, "vi", orderInfo, Long.valueOf(amount), "test MoMo", null, requestType,
//                    returnUrl, notifyUrl, "test store ID", extraData, null, autoCapture, null, signRequest);
//        } catch (Exception e) {
//            logger.error("[PaymentRequest] "+ e);
//        }
//
//        return null;
//    }

//    public HttpResponseDTO sendToMoMo(String payload) {
//        try {
//
//            String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
//            HttpRequestDTO httpRequest = new HttpRequestDTO("POST", endpoint, payload, "application/json");
//
//            Request request = createRequest(httpRequest);
//
//            logger.debug("[HttpPostToMoMo] Endpoint:: " + httpRequest.getEndpoint() + ", RequestBody:: " + httpRequest.getPayload());
//
//            Response result = client.newCall(request).execute();
//            HttpResponseDTO response = new HttpResponseDTO(result.code(), result.body().string(), result.headers());
//
//            logger.info("[HttpResponseFromMoMo] " + response.toString());
//
//            return response;
//        } catch (Exception e) {
//            logger.error("[ExecuteSendToMoMo] "+ e);
//        }
//        return null;
//    }

    private Request createRequest(HttpRequestDTO request) {
        RequestBody body = RequestBody.create(MediaType.get(request.getContentType()), request.getPayload());
        return new Request.Builder()
                .method(request.getMethod(), body)
                .url(request.getEndpoint())
                .build();
    }

//    public String getBodyAsString(Request request) throws IOException {
//        Buffer buffer = new Buffer();
//        RequestBody body = request.body();
//        body.writeTo(buffer);
//        return buffer.readUtf8();
//    }

}

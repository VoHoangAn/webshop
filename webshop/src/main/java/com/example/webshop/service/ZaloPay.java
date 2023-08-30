package com.example.webshop.service;


// Java version "1.8.0_201"
import com.example.webshop.order.Order;
import com.example.webshop.orderDetail.OrderDetail;
import org.apache.http.NameValuePair; // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject; // https://mvnrepository.com/artifact/org.json/json
import com.example.webshop.util.HMACUtil; // tải về ở mục DOWNLOADS
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
public class ZaloPay {
    private static Map<String, String> config = new HashMap<String, String>(){{
        put("app_id", "553");
        put("key1", "9phuAOYhan4urywHTh0ndEXiV3pKHr5Q");
        put("key2", "Iyz2habzyr7AG8SgvoBCbKwKi3UzlLi3");
        put("createOrder", "https://sb-openapi.zalopay.vn/v2/create");
        put("getOrderStatus", "https://sb-openapi.zalopay.vn/v2/query");
    }};

    private static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    private final Logger logger = LoggerFactory.getLogger(ZaloPay.class);
    public JSONObject createOrder(Order orderFromWeb) throws Exception
    {
        Random rand = new Random();
        int random_id = rand.nextInt(1000000);
        final Map embed_data = new HashMap(){{
            put("redirecturl","https://docs.zalopay.vn/result");
        }};

        final JSONArray item = new JSONArray();
        for (OrderDetail od:orderFromWeb.getOrderDetails()) {
            JSONObject orderDetail = new JSONObject();
            orderDetail.put(OrderDetail.Fields.productId,od.getId());
            orderDetail.put(OrderDetail.Fields.productName,od.getProductName());
            orderDetail.put(OrderDetail.Fields.quantity,od.getQuantity());
            orderDetail.put(OrderDetail.Fields.price,od.getPrice());
            orderDetail.put(OrderDetail.Fields.sale,od.getSale());
            orderDetail.put(OrderDetail.Fields.orderId,orderFromWeb.getId());
            item.put(orderDetail);
        }

        Map<String, Object> order = new HashMap<String, Object>(){{
            put("app_id", config.get("app_id"));
            put("app_trans_id", getCurrentTimeString("yyMMdd") +"_"+ random_id); // translation missing: vi.docs.shared.sample_code.comments.app_trans_id
            put("app_time", System.currentTimeMillis()); // miliseconds
            put("app_user", orderFromWeb.getUserName()+"#"+orderFromWeb.getUserId());
            put("amount", orderFromWeb.getTotalAmount());
            put("description", "Lazada - Payment for the order #"+random_id);
            put("bank_code", "zalopayapp");
            put("item", item.toString());
            put("embed_data", new JSONObject(embed_data).toString());
        }};

        orderFromWeb.setZaloTransactionId(order.get("app_trans_id").toString());

        // app_id +”|”+ app_trans_id +”|”+ appuser +”|”+ amount +"|" + app_time +”|”+ embed_data +"|" +item
        String data = order.get("app_id") +"|"+ order.get("app_trans_id") +"|"+ order.get("app_user") +"|"+ order.get("amount")
                +"|"+ order.get("app_time") +"|"+ order.get("embed_data") +"|"+ order.get("item");
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data);
        order.put("mac", mac);
        orderFromWeb.setMac(mac);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(config.get("createOrder"));

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, Object> e : order.entrySet()) {
            params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
        }

        // Content-Type: application/x-www-form-urlencoded
        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }

        return new JSONObject(resultJsonStr.toString());
//        JSONObject result = new JSONObject(resultJsonStr.toString());

//        for (String key : result.keySet()) {
//            System.out.format("%s = %s\n", key, result.get(key));
//        }
    }

    public int getOrderStatus(String app_trans_id, String mac1) throws URISyntaxException, IOException {
        String data = config.get("app_id") +"|"+ app_trans_id  +"|"+ config.get("key1"); // appid|app_trans_id|key1
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app_id", config.get("app_id")));
        params.add(new BasicNameValuePair("app_trans_id", app_trans_id));
        params.add(new BasicNameValuePair("mac", mac));

        URIBuilder uri = new URIBuilder(config.get("getOrderStatus"));
        uri.addParameters(params);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(uri.build());
        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }

        JSONObject result = new JSONObject(resultJsonStr.toString());

        for (String key : result.keySet()) {
            logger.error(String.format("%s = %s\n", key, result.get(key)));
        }
        return (int) result.get("return_code");
    }


}
package com.example.webshop.order;

import com.example.webshop.dto.IPNRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/order")
@RequiredArgsConstructor
public class OrderController {
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    @GetMapping(path = "/getAll") //@Param(value = "userId") Long userId   |
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestParam("orderInfo") String orderInfo) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Order order =mapper.readValue(orderInfo, Order.class);

            orderService.create(order);
            return ResponseEntity.ok("ordered");
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw e;
        }
    }

    @PostMapping(path = "/momoNotification")
    public ResponseEntity<HttpStatus> create(@RequestBody IPNRequestDTO momoResponse) throws Exception {
        try {
            LOGGER.info(momoResponse.toString());
//            orderService.create(order);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw e;
        }
    }

}

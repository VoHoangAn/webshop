//package com.example.webshop.util;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.concurrent.*;
//import java.util.function.Supplier;
//
//public class Polling {
//    private static final Logger logger= LoggerFactory.getLogger(Polling.class);
//    public static void runRequestEveryMin(Supplier supplier) {
//        Executor delayed = CompletableFuture.delayedExecutor(15L, TimeUnit.SECONDS);
//        CompletableFuture.supplyAsync(supplier, delayed)
//                .thenAccept((result)->logger.info(result.toString()))
//                .join();
//    }
//}

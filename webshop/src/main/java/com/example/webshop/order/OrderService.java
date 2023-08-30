/*
package com.example.webshop.product;

import com.example.webshop.service.FileStorageService;
import com.example.webshop.user.User;
import com.example.webshop.util.CloudinaryUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
//    private final CategoryFieldRepository categoryFieldRepository;
    private final ProductRepositoryImpl productRepository;
    private final CloudinaryUtil cloudinaryUtil;
    private final FileStorageService storageService;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final String imgURLPrefix = "https://res.cloudinary.com/vohoangan/image/upload/webshop/";

    public Product create(Product category,List<CategoryField> categoryFields, MultipartFile file) throws IOException {
        boolean isExisted = categoryRepository.existsByName(category.getName());
        if(isExisted) {throw new IllegalStateException("category has existed");}

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        category.setCreatedBy(user.getId());

        storageService.save(file);
        cloudinaryUtil.upload(file.getOriginalFilename());
        category.setImage(imgURLPrefix+file.getOriginalFilename());

        categoryFields.forEach(item->category.addCategoryFields(item));

        return categoryRepository.save(category);
    }

    @Transactional
    public void update(Long categoryId, Category newCategory, MultipartFile file) throws IOException {
        Category oldCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()->new IllegalStateException("category hasn't existed"));

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(oldCategory.getCreatedBy()!=user.getId()) {
            throw new IllegalStateException("You are not the owner of this");
        }

        if(!oldCategory.getImage().equals("https://res.cloudinary.com/vohoangan/image/upload/webshop/"
                +file.getOriginalFilename())) {
            storageService.save(file);
            cloudinaryUtil.upload(file.getOriginalFilename());
            oldCategory.setImage(imgURLPrefix+file.getOriginalFilename());
        }

        oldCategory.setName(newCategory.getName());
        var difference = compareOldvsNewCategoryFields(oldCategory.getCategoryFields(),new ArrayList<>(newCategory.getCategoryFields()));

//        logger.error(difference.addedItem.toString());
//        logger.error(difference.updatedAndRemovedItem.toString());
        difference.updatedAndRemovedItem.get("deleted").forEach(id->oldCategory.removeCategoryFields(
                oldCategory.getCategoryFields().stream().filter(item->item.getId().equals(id)).findFirst().get()
        ));
//        logger.error(oldCategory.getCategoryFields().toString());
//        logger.error(newCategory.getCategoryFields().toString());
        difference.updatedAndRemovedItem.get("updated").forEach(id->{
            CategoryField newCategoryField = newCategory.getCategoryFields().stream()
                    .filter(item->{
                        if(item.getId()!=null) {
                            return item.getId().equals(id);
                        }
                        return false;
                    }).findFirst().get();

            CategoryField oldCategoryField = oldCategory.getCategoryFields().stream()
                    .filter(item->item.getId().equals(id)).findFirst().get();

            oldCategoryField.setName(newCategoryField.getName());
            oldCategoryField.setValue(newCategoryField.getValue());
            oldCategoryField.setMeasure(newCategoryField.getMeasure());
            oldCategoryField.setFilterable(newCategoryField.isFilterable());
        });
        difference.addedItem.forEach(item->oldCategory.addCategoryFields(item));

//        return categoryRepository.update(category.getId(),category.getName(),category.getImage());
    }

    private StatisticDifference compareOldvsNewCategoryFields(List<CategoryField> oldCategoryField, List<CategoryField> newCategoryField) {
        StatisticDifference difference = new StatisticDifference();

        if(oldCategoryField.size()>0) {
            oldCategoryField.forEach(oldItem -> {
                Long curId = oldItem.getId();
                Optional<CategoryField> foundItem = newCategoryField.stream()
                        .filter(newItem -> curId.equals(newItem.getId())).findFirst();
                if (foundItem.isEmpty()) {
                    difference.updatedAndRemovedItem.put("deleted", curId);
                } else {
                    if (!isCategoryFieldUnchanged(oldItem, foundItem.get())) {
                        difference.updatedAndRemovedItem.put("updated", curId);
                    }
                    newCategoryField.remove(foundItem.get());
                }
            });
for (int index = 0; index < oldCategoryField.size(); index++) {
                CategoryField oldItem = oldCategoryField.get(index);
                Long curId = oldItem.getId();
                Optional<CategoryField> foundItem = newCategoryField.stream()
                        .filter(newItem -> curId.equals(newItem.getId())).findFirst();
                if (foundItem.isEmpty()) {
                    difference.updatedAndRemovedItem.put("deleted", index);
                } else {
                    if (!isCategoryFieldUnchanged(oldItem, foundItem.get())) {
                        difference.updatedAndRemovedItem.put("updated", index);
                    }
                    newCategoryField.remove(foundItem.get());
                }
            }

        }
else {
            oldCategoryField.forEach(item->difference.updatedAndRemovedItem.put("deleted", item.getId()));
        }

        difference.addedItem.addAll(newCategoryField);
        return difference;
    }

    private class StatisticDifference {
        ListMultimap<String,Long> updatedAndRemovedItem = ArrayListMultimap.create();
        List<CategoryField> addedItem = new ArrayList<>();
        public StatisticDifference(){}
    }

    private boolean isCategoryFieldUnchanged(CategoryField oldCategoryField,CategoryField newCategoryField) {
        return oldCategoryField.getName().equals(newCategoryField.getName()) &&
                oldCategoryField.getValue().equals(newCategoryField.getValue()) &&
                oldCategoryField.getMeasure().equals(newCategoryField.getMeasure());
    }

    public Category get(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new IllegalStateException("Category hasnt existed"));
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public void remove(Long id) {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new IllegalStateException("address not exist"));
        if(Objects.equals(category.getCreatedBy(), user.getId())) {
            category.setAvailable(false);
            category.getCategoryFields().forEach(item->item.setAvailable(false));
        } else {
            throw new IllegalStateException("you dont have permission");
        }
    }
}
*/


package com.example.webshop.order;

import com.example.webshop.cart.Cart;
import com.example.webshop.cart.CartRepositoryImpl;
import com.example.webshop.cart.CartService;
import com.example.webshop.dto.PaymentResponseDTO;
import com.example.webshop.orderDetail.OrderDetail;
import com.example.webshop.orderDetail.OrderDetailRepositoryImpl;
import com.example.webshop.product.Product;
import com.example.webshop.product.ProductRepositoryImpl;
import com.example.webshop.service.MomoPay;
import com.example.webshop.user.User;
import com.example.webshop.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService  {
    private final OrderRepositoryImpl orderRepository;
    private final OrderDetailRepositoryImpl orderDetailRepository;
    private final ProductRepositoryImpl productRepository;
    private final CartRepositoryImpl cartRepository;
    private final UserRepositoryImpl userRepository;
//    private final ZaloPay zaloPay;
    private final MomoPay momoPay;
    private final Logger logger = LoggerFactory.getLogger(CartService.class);

//    @Autowired
//    public void setMomoPay(MomoPay momoPay) {
//        this.momoPay=momoPay;
//    }

    //@Transactional//(propagation = Propagation.REQUIRES_NEW)
    public void create(Order order) throws Exception {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        order.setUserId(user.getId());
        order.setUserName(user.getUsername());


        orderProcessHandler(order);
        logger.error("dtail0: "+order.getOrderDetails());
        Order savedOrder = orderRepository.save(order);
        logger.info("ordr: "+savedOrder);
        String requestId = UUID.randomUUID().toString();
        PaymentResponseDTO captureWalletMoMoResponse = momoPay.createOrder(savedOrder.getId().toString(), requestId,
                savedOrder.getTotalAmount().toString(), "Pay With MoMo", "https://google.com.vn",
                "http://localhost:8080/order/momoNotification", "ew0KImVtYWlsIjogImh1b25neGRAZ21haWwuY29tIg0KfQ==", "captureWallet", Boolean.TRUE);

        momoPay.getOrderStatus(savedOrder.getId(),order,requestId);
//                .thenAcceptAsync((rs)->{
//                    logger.info("rs: "+rs);
//                    if(rs.equals(0)) {
//                        paymentSuccessHandler(savedOrder.getId(),order);
//                    } else {
//                        paymentFailureHandler(savedOrder);
//                    }
//                });
//        zaloPay.getOrderStatus(order.getZaloTransactionId(),order.getMac());
    }

   /* public void orderProcess(List<Tuple> productQuantityInCart,List<Tuple> productQuantityInTotal) {
        for (int i = 0; i < productQuantityInCart.size(); i++) {
            Long curProductId = productQuantityInTotal.get(i).get(0,Long.class);
            Long remainQuantity = productQuantityInTotal.get(i).get(1,Long.class)-productQuantityInCart.get(i).get(1,Long.class);
            if(remainQuantity>=0) {
                productRepository.updateQuantityById(remainQuantity,curProductId);
                if(remainQuantity==0){
                    productRepository.updateSoldOutById(curProductId);
                }
            } else {
                Optional<Product> curProduct = productRepository.findById(curProductId);
                throw new IllegalStateException("Product name '"+curProduct.get().getName()
                        +"' that you want to buy only remain: "+curProduct.get().getQuantity());
            }
        }
    }*/

    public void orderProcessHandler(Order order) throws Exception {
        List<OrderDetail> orderedItems = new ArrayList<>();

        List<Cart> carts = cartRepository.findAllByUserId(order.getUserId())
                .orElseThrow();
        BigDecimal totalAmount = fillProductInfoForOrderedItem(carts,order.getUserId(),orderedItems);
        order.setOrderDetails(orderedItems);
        order.setTotalAmount(totalAmount.setScale(0,BigDecimal.ROUND_UP));

//        JSONObject zaloResponse = zaloPay.createOrder(order);
//        logger.error("zaloResponse: "+zaloResponse);
        userRepository.changeUserStateToPaying(order.getUserId());
    }

    private BigDecimal fillProductInfoForOrderedItem(List<Cart> carts, Long orderId,
                                                     List<OrderDetail> orderDetails) {
        BigDecimal totalAmount= BigDecimal.valueOf(0);

        List<Cart> sortedCarts = carts.stream()
                .sorted((Comparator.comparing(Cart::getProductId)))
                .toList();
        List<Long> productIdsInCart = sortedCarts.stream()
                .map(item->item.getProductId())
                .toList();
        List<Product> productInfosInCart = productRepository.findSomeByIdsOrderById(productIdsInCart);

        if(productInfosInCart.size()<sortedCarts.size()) throw new IllegalStateException("Product not exist");

        for (int i = 0; i < productInfosInCart.size(); i++) {
            OrderDetail orderedItem = new OrderDetail();
            Product curProduct = productInfosInCart.get(i);

//            orderedItem.setId(sortedCarts.get(i).getId());
            orderedItem.setProductId(curProduct.getId());
            orderedItem.setPrice(curProduct.getPrice());
            orderedItem.setSale(curProduct.getSale());
            orderedItem.setProductName(curProduct.getName());
            orderedItem.setQuantity(sortedCarts.get(i).getQuantity());
            orderedItem.setOrderId(orderId);

            BigDecimal calcProductPrice = curProduct.getPrice().multiply(
                    BigDecimal.valueOf(sortedCarts.get(i).getQuantity()));
            if(curProduct.getSale()!=0) {
                calcProductPrice = calcProductPrice.multiply(BigDecimal.valueOf(1-curProduct.getSale()/100));
            }
            totalAmount = totalAmount.add(calcProductPrice);

            orderDetails.add(orderedItem);
        }
        return totalAmount;
    }

  /*  public void paymentSuccessHandler(Long orderId, Order order) {
        logger.info("paymentSuccessHandler"+orderId+order);
        try {
            userRepository.changeUserStateBackToUser(order.getUserId());
            orderRepository.changeOrderStatusToOrdered(orderId);
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
    }*/

    public List<Order> getAll() {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<List<Order>> orders =  orderRepository.findAllByUserId(user.getId());
        return orders.isEmpty() ? null : orders.get();
    }

}

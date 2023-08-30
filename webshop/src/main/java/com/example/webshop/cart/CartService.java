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


package com.example.webshop.cart;

import com.example.webshop.dto.CartDTO;
import com.example.webshop.product.Product;
import com.example.webshop.product.ProductRepositoryImpl;
import com.example.webshop.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductRepositoryImpl productRepository;
    private final CartRepositoryImpl cartRepository;
    private final Logger logger = LoggerFactory.getLogger(CartService.class);

    public List<Cart> add(List<Cart> carts) throws IOException {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        carts.forEach(c->c.setUserId(user.getId()));
//        List<Cart> carts = fillProductInfoForCartItem(cartDTOS, user);
        return cartRepository.saveAll(carts);
    }

    @Transactional
    public void update(List<Cart> newCarts) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Cart> newCart = fillProductInfoForCartItem(newCartDTOs, user);

        List<Cart> oldCart = cartRepository.findAllByUserId(user.getId()).get();
        List<Long> deletedItem = findDeletedItem(oldCart,newCarts);
        cartRepository.deleteAllById(deletedItem);

        cartRepository.update(newCarts,user.getId());
    }

    private List<Cart> fillProductInfoForCartItem(List<CartDTO> cartDTOs, Long userId) {
        List<Cart> carts = new ArrayList<>();

        List<CartDTO> cartDTOsSorted = cartDTOs.stream()
                .sorted((Comparator.comparing(CartDTO::getProductId)))
                .toList();
        List<Long> productIdsInCart = cartDTOsSorted.stream()
                .map(item->item.getProductId())
                .toList();
        List<Product> productInfosInCart = productRepository.findSomeByIdsOrderById(productIdsInCart);

//        logger.error(cartDTOsSorted.stream().map(CartDTO::getProductId).toList().toString());
//        logger.error(productIdsInCart.toString());
//        logger.error("productInfosInCart: "+ productInfosInCart.stream().map(Product::getId).toList());

        if(productInfosInCart.size()<cartDTOsSorted.size()) throw new IllegalStateException("Product not exist");

        for (int i = 0; i < productInfosInCart.size(); i++) {
            Cart cartItem = new Cart();
            cartItem.setId(cartDTOsSorted.get(i).getId());
            cartItem.setProductId(productInfosInCart.get(i).getId());
            cartItem.setPrice(productInfosInCart.get(i).getPrice());
            cartItem.setSale(productInfosInCart.get(i).getSale());
            cartItem.setProductName(productInfosInCart.get(i).getName());
            cartItem.setQuantity(cartDTOsSorted.get(i).getQuantity());
            cartItem.setUserId(userId);

            carts.add(cartItem);
        }
        return carts;
    }

    private List<Long> findDeletedItem(List<Cart> oldCart, List<Cart> newCart) {
        List<Long> oldItemsId = oldCart.stream().map(item->item.getId()).collect(Collectors.toList());
        List<Long> newItemsId = newCart.stream().map(item->item.getId())
                .filter(Predicate.not(Objects::isNull)).collect(Collectors.toList());
        if(newItemsId.isEmpty()) return oldItemsId;
        return oldItemsId.stream().filter(Predicate.not(newItemsId::contains)).collect(Collectors.toList());
    }

    public List<Cart> getAll() {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<List<Cart>> carts =  cartRepository.findAllByUserId(user.getId());
        return carts.isEmpty() ? null : carts.get();
    }

    public void delete(Long id) {
        cartRepository.deleteById(id);
    }

}

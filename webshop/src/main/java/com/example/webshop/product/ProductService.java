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


package com.example.webshop.product;

import com.example.webshop.dto.ProductWithPageDTO;
import com.example.webshop.service.FileStorageService;
import com.example.webshop.user.User;
import com.example.webshop.util.CloudinaryUtil;
import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Product create(Product product, MultipartFile file) throws IOException {
        boolean isExisted = productRepository.existsByName(product.getName());
        if(isExisted) {throw new IllegalStateException("category has existed");}

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        product.setUserId(user.getId());

        storageService.save(file);
        cloudinaryUtil.upload(file.getOriginalFilename());
        product.setImage(imgURLPrefix+file.getOriginalFilename());

        return productRepository.save(product);
    }

    @Transactional
    public void update(Product newProduct, MultipartFile file) throws IOException {
        Product oldProduct = productRepository.findById(newProduct.getId()).get();
        if(oldProduct ==null) {throw new IllegalStateException("product hasnt existed");}

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!oldProduct.getUserId().equals(user.getId())) {
            throw new IllegalStateException("You are not the owner of this");
        }

        if(!oldProduct.getImage().equals("https://res.cloudinary.com/vohoangan/image/upload/webshop/"
                +file.getOriginalFilename())) {
            storageService.save(file);
            cloudinaryUtil.upload(file.getOriginalFilename());
            newProduct.setImage(imgURLPrefix+file.getOriginalFilename());
        }

        productRepository.update(newProduct);
//
//        oldCategory.setName(newCategory.getName());
//        var difference = compareOldvsNewCategoryFields(oldCategory.getCategoryFields(),new ArrayList<>(newCategory.getCategoryFields()));
//
////        logger.error(difference.addedItem.toString());
////        logger.error(difference.updatedAndRemovedItem.toString());
//        difference.updatedAndRemovedItem.get("deleted").forEach(id->oldCategory.removeCategoryFields(
//                oldCategory.getCategoryFields().stream().filter(item->item.getId().equals(id)).findFirst().get()
//        ));
////        logger.error(oldCategory.getCategoryFields().toString());
////        logger.error(newCategory.getCategoryFields().toString());
//        difference.updatedAndRemovedItem.get("updated").forEach(id->{
//            CategoryField newCategoryField = newCategory.getCategoryFields().stream()
//                    .filter(item->{
//                        if(item.getId()!=null) {
//                            return item.getId().equals(id);
//                        }
//                        return false;
//                    }).findFirst().get();
//
//            CategoryField oldCategoryField = oldCategory.getCategoryFields().stream()
//                    .filter(item->item.getId().equals(id)).findFirst().get();
//
//            oldCategoryField.setName(newCategoryField.getName());
//            oldCategoryField.setValue(newCategoryField.getValue());
//            oldCategoryField.setMeasure(newCategoryField.getMeasure());
//            oldCategoryField.setFilterable(newCategoryField.isFilterable());
//        });
//        difference.addedItem.forEach(item->oldCategory.addCategoryFields(item));

//        return categoryRepository.update(category.getId(),category.getName(),category.getImage());
    }
//
//    private StatisticDifference compareOldvsNewCategoryFields(List<CategoryField> oldCategoryField, List<CategoryField> newCategoryField) {
//        StatisticDifference difference = new StatisticDifference();
//
//        if(oldCategoryField.size()>0) {
//            oldCategoryField.forEach(oldItem -> {
//                Long curId = oldItem.getId();
//                Optional<CategoryField> foundItem = newCategoryField.stream()
//                        .filter(newItem -> curId.equals(newItem.getId())).findFirst();
//                if (foundItem.isEmpty()) {
//                    difference.updatedAndRemovedItem.put("deleted", curId);
//                } else {
//                    if (!isCategoryFieldUnchanged(oldItem, foundItem.get())) {
//                        difference.updatedAndRemovedItem.put("updated", curId);
//                    }
//                    newCategoryField.remove(foundItem.get());
//                }
//            });
//            for (int index = 0; index < oldCategoryField.size(); index++) {
//                CategoryField oldItem = oldCategoryField.get(index);
//                Long curId = oldItem.getId();
//                Optional<CategoryField> foundItem = newCategoryField.stream()
//                        .filter(newItem -> curId.equals(newItem.getId())).findFirst();
//                if (foundItem.isEmpty()) {
//                    difference.updatedAndRemovedItem.put("deleted", index);
//                } else {
//                    if (!isCategoryFieldUnchanged(oldItem, foundItem.get())) {
//                        difference.updatedAndRemovedItem.put("updated", index);
//                    }
//                    newCategoryField.remove(foundItem.get());
//                }
//            }
//
//        }
//        else {
//            oldCategoryField.forEach(item->difference.updatedAndRemovedItem.put("deleted", item.getId()));
//        }
//
//        difference.addedItem.addAll(newCategoryField);
//        return difference;
//    }
//
//    private class StatisticDifference {
//        ListMultimap<String,Long> updatedAndRemovedItem = ArrayListMultimap.create();
//        List<CategoryField> addedItem = new ArrayList<>();
//        public StatisticDifference(){}
//    }
//
//    private boolean isCategoryFieldUnchanged(CategoryField oldCategoryField,CategoryField newCategoryField) {
//        return oldCategoryField.getName().equals(newCategoryField.getName()) &&
//                oldCategoryField.getValue().equals(newCategoryField.getValue()) &&
//                oldCategoryField.getMeasure().equals(newCategoryField.getMeasure());
//    }
//
    public Product get(Long id) {
        return productRepository.findById(id).orElseThrow(()->new IllegalStateException("Product hasnt existed"));
    }

    public List<Product> getAll() {
        return productRepository.findAllAvailableProduct();
    }

    @Transactional
    public void remove(Long id) {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = productRepository.findById(id)
                .orElseThrow(()->new IllegalStateException("Product not exist"));
        if(Objects.equals(product.getUserId(), user.getId())) {
            product.setAvailable(false);
        } else {
            throw new IllegalStateException("you dont have permission");
        }
    }

    public ProductWithPageDTO find(int page, int size, Map<String,String> filterCriteria, Map<String,String> sort) {
        Page<Product> foundItem = productRepository.find(page,size,filterCriteria,sort);
        List<Product> products = new ArrayList<>();
        products.addAll(foundItem.getContent());
        ProductWithPageDTO productWithPageDTO = new ProductWithPageDTO();
        logger.error(products.get(0).toString());
        productWithPageDTO.setProducts(products);
        productWithPageDTO.setCurrentPage(foundItem.getNumber()+1);
        productWithPageDTO.setSize(foundItem.getSize());
        productWithPageDTO.setTotalPage(foundItem.getTotalPages());
        return productWithPageDTO;
    }

}

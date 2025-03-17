package com.incognito.modulith.catalog.web.controllers;

import com.incognito.modulith.catalog.Product;
import com.incognito.modulith.catalog.ProductService;
import com.incognito.modulith.catalog.domain.ProductNotFoundException;
import com.incognito.modulith.common.models.PagedResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
class ProductController {
    private final ProductService productService;

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int page) {
        log.info("Fetching products for page: {}", page);
        return productService.getProducts(page);
    }

    @GetMapping("/{code}")
    Product getProductByCode(@PathVariable String code){
        log.info("Fetching product by code: {}", code);
        return productService.getByCode(code)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}

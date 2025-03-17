package com.incognito.modulith.catalog;



import com.incognito.modulith.common.models.PagedResult;

import java.util.Optional;

public interface ProductService {
     PagedResult<Product> getProducts(int pageNo);

     Optional<Product> getByCode(String code);
}

package kitchenpos.product.application;

import java.util.stream.Collectors;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product newProduct = new Product(productRequest.getName(), productRequest.getPrice());
        Product savedProduct = productDao.save(newProduct);

        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> allProducts = productDao.findAll();
        return allProducts.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}

package kitchenpos.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@Service
public class ProductService {
    private final ProductRepository productDao;

    public ProductService(final ProductRepository productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product product = productRequest.toEntity();

        return ProductResponse.of(productDao.save(product));
    }

    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();

        return ProductResponse.ofList(products);
    }
}

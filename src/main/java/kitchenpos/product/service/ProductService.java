package kitchenpos.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponses;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long create(final ProductCreateRequest request) {
        Product product = request.toEntity();
        return productRepository.save(product).getId();
    }

    public ProductResponses list() {
        List<Product> products = productRepository.findAll();
        return ProductResponses.from(products);
    }
}

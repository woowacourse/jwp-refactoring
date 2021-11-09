package kitchenpos.application.product.service;

import java.util.List;
import kitchenpos.application.product.domain.Product;
import kitchenpos.application.product.domain.ProductRepository;
import kitchenpos.application.product.service.ProductMapper;
import kitchenpos.application.product.service.ProductRequest;
import kitchenpos.application.product.service.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productMapper.mapFrom(productRequest);
        return ProductResponse.of(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.listOf(productRepository.findAll());
    }
}

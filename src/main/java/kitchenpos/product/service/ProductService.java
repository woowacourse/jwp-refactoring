package kitchenpos.product.service;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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

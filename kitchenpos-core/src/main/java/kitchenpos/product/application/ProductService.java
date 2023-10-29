package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.common.dto.request.CreateProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final CreateProductRequest productRequest) {
        final Product product = Product.of(productRequest.getName(), productRequest.getPrice());
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }
}

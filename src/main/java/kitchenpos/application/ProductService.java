package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product getById(Long productId) {
        return productRepository.findById(productId)
                                .orElseThrow(() -> new CustomException(ExceptionType.PRODUCT_NOT_FOUND, String.valueOf(productId)));
    }
}

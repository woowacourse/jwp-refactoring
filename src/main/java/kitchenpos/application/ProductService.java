package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.product.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        return productRepository.save(createProductDataByRequest(request));
    }

    private Product createProductDataByRequest(final ProductCreateRequest request) {
        return new Product(request.getName(), request.getPrice());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}

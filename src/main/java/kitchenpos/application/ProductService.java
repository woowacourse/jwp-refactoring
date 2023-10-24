package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.product.repository.ProductRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.request.CreateProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final CreateProductRequest request) {
        return productRepository.save(new Product(request.getName(), request.getPrice()));
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}

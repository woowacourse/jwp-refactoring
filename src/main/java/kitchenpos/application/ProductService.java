package kitchenpos.application;

import kitchenpos.domain.menu.Product;
import kitchenpos.dto.CreateProductRequest;
import kitchenpos.repository.ProductRepository;
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
    public Product create(final CreateProductRequest request) {
        return productRepository.save(request.toProduct());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}

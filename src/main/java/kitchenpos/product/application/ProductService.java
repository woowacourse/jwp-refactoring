package kitchenpos.product.application;

import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
    public Long create(final ProductCreateRequest request) {
        return productRepository.save(new Product(request.getName(), request.getPrice())).getId();
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}

package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest product) {
        return productRepository.save(new Product(product.getName(), Price.of(product.getPrice())));
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}

package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dtos.ProductRequest;
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
    public Product create(final ProductRequest request) {
        final Product product = Product.builder()
                .name(request.getName())
                .price(new Price(request.getPrice()))
                .build();

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}

package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dtos.ProductRequest;
import kitchenpos.dao.ProductRepository;
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
        final Long price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(0L) < 0) {
            throw new IllegalArgumentException();
        }

        final Product product = Product.builder()
                .name(request.getName())
                .price(BigDecimal.valueOf(request.getPrice()))
                .build();

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}

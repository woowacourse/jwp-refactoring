package kitchenpos.application;

import kitchenpos.exception.KitchenException;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new KitchenException("상품 가격은 1원 이상입니다.");
        }

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}

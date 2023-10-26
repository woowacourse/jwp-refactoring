package kitchenpos.product.application;

import java.math.BigDecimal;
import kitchenpos.menu.application.ProductTotalPriceValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductTotalPriceValidatorImpl implements ProductTotalPriceValidator {

    private final ProductRepository productRepository;

    public ProductTotalPriceValidatorImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public BigDecimal getTotalPriceThrowIfNotExist(final Long productId, final BigDecimal quantity) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);

        return product.getTotalPrice(quantity);
    }
}

package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(final BigDecimal price, final List<MenuProduct> menuProducts) {
        validatePrice(price);
        final BigDecimal totalMenuProductPrice = calculateTotalMenuProductPrice(menuProducts);
        if (price.compareTo(totalMenuProductPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateTotalMenuProductPrice(final List<MenuProduct> menuProducts) {
        final int menuProductPrice = menuProducts.stream()
                .mapToInt(menuProduct -> {
                    final Long productId = menuProduct.getProductId();
                    final Product product = productRepository.findById(productId)
                            .orElseThrow(IllegalArgumentException::new);
                    return product.calculatePrice(menuProduct.getQuantity()).intValue();
                }).reduce(0, Integer::sum);
        return BigDecimal.valueOf(menuProductPrice);
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }
}

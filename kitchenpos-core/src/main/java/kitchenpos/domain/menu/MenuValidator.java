package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(final ProductRepository productRepository, final MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(final Menu menu) {
        validateMenuGroup(menu.getMenuGroupId());
        validatePrice(menu.getPrice());
        validateSum(menu.getPrice(), menu.getMenuProducts());
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSum(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = menuProducts.stream()
                .map(this::calculateMenuProductPrice)
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateMenuProductPrice(final MenuProduct menuProduct) {
        final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }
}

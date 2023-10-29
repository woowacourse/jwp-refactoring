package kitchenpos.menu.domain.validator;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Component;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(final ProductRepository productRepository,
                         final MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateCreate(final BigDecimal price,
                               final Long menuGroupId,
                               final List<MenuProduct> menuProducts) {
        validateExistMenuGroup(menuGroupId);
        validatePriceOverZero(price);
        validateTotalPrice(price, menuProducts);
    }

    private void validateExistMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePriceOverZero(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTotalPrice(final BigDecimal price,
                                    final List<MenuProduct> menuProducts) {
        final BigDecimal totalPrice = calculateAllProductPrice(menuProducts);
        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateAllProductPrice(final List<MenuProduct> menuProducts) {
        final Map<Long, Product> products = findProducts(menuProducts);
        return menuProducts.stream()
                .map(menuProduct -> calculateTotalPrice(menuProduct, products))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Long, Product> findProducts(final List<MenuProduct> menuProducts) {
        final List<Long> productIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
        final List<Product> products = productRepository.findByIdIn(productIds);
        return products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    private BigDecimal calculateTotalPrice(final MenuProduct menuProduct,
                                           final Map<Long, Product> products) {
        final Product product = products.get(menuProduct.getProductId());
        return product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
    }
}

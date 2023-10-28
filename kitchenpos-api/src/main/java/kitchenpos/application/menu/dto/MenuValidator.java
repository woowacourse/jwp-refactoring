package kitchenpos.application.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.MenuProduct;
import kitchenpos.product.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.util.BigDecimalUtil;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateMenuPriceNotBiggerThanMenuProductsTotalPrice(final BigDecimal price,
                                                                     final List<MenuProduct> menuProducts) {
        final BigDecimal menuProductsTotalPrice = getMenuProductsTotalPrice(menuProducts);
        BigDecimalUtil.valueForCompare(price)
                .throwIfBiggerThan(menuProductsTotalPrice, IllegalArgumentException::new);
    }

    private BigDecimal getMenuProductsTotalPrice(final List<MenuProduct> menuProducts) {
        final List<BigDecimal> menuProductTotalPrices = menuProducts.stream()
                .map(this::getProductTotalPrice)
                .collect(Collectors.toList());

        return BigDecimalUtil.sum(menuProductTotalPrices);
    }

    private BigDecimal getProductTotalPrice(final MenuProduct menuProduct) {
        final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);

        return product.calculateTotalPrice(BigDecimal.valueOf(menuProduct.getQuantity()));
    }
}

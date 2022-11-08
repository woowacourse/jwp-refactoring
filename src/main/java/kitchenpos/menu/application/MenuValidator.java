package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.MenuProductAmountException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.ProductValidator;
import kitchenpos.product.domain.Price;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductValidator productValidator;

    public MenuValidator(final ProductValidator productValidator) {
        this.productValidator = productValidator;
    }

    public void validate(final Menu menu) {
        validateProductIdsExists(menu);
        validatePrice(menu);
    }

    private void validatePrice(final Menu menu) {
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        final Price menuPrice = menu.getPrice();
        if (menuPriceIsExpansiveThanMenuProducts(menuProducts, menuPrice)) {
            throw new InvalidMenuPriceException();
        }
    }

    private boolean menuPriceIsExpansiveThanMenuProducts(final List<MenuProduct> menuProducts,
                                                                final Price menuPrice) {
        final List<Long> productIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
        final Price productAmountSum = productValidator.calculateAmountSum(productIds)
                .orElseThrow(MenuProductAmountException::new);
        return menuPrice.isExpansiveThan(productAmountSum);
    }

    private void validateProductIdsExists(final Menu menu) {
        final List<Long> productIds = menu.getMenuProducts().stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
        if (!productValidator.existsProductsByIdIn(productIds)) {
            throw new ProductNotFoundException();
        }
    }
}

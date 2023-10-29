package kitchenpos.menu.domain;

import static java.util.stream.Collectors.toUnmodifiableList;
import static kitchenpos.menu.domain.exception.MenuExceptionType.MENU_GROUP_IS_NOT_FOUND;
import static kitchenpos.menu.domain.exception.MenuExceptionType.PRICE_IS_BIGGER_THAN_MENU_PRODUCT_PRICES_SUM;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.exception.MenuException;
import kitchenpos.menu_group.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.vo.Price;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateMenuCreate(final Menu menu) {
        validateInMenuGroup(menu);
        validatePrice(menu);
    }

    private void validateInMenuGroup(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new MenuException(MENU_GROUP_IS_NOT_FOUND);
        }
    }

    private void validatePrice(final Menu menu) {
        final List<Product> products = findProducts(menu);
        final Price sum = sumMenuProductPrices(products, menu);
        final Price price = menu.getPrice();

        if (price.isBigger(sum)) {
            throw new MenuException(PRICE_IS_BIGGER_THAN_MENU_PRODUCT_PRICES_SUM);
        }
    }

    private List<Product> findProducts(final Menu menu) {
        final List<Long> productIds = menu.getMenuProducts()
            .stream()
            .map(MenuProduct::getProductId)
            .collect(toUnmodifiableList());

        return productRepository.findAllById(productIds);
    }

    private Price sumMenuProductPrices(
        final List<Product> products,
        final Menu menu
    ) {
        final Map<Long, Price> productPriceMap = products.stream()
            .collect(Collectors.toUnmodifiableMap(Product::getId, Product::getPrice));

        return menu.getMenuProducts()
            .stream()
            .map(menuProduct -> productPriceMap.get(menuProduct.getProductId())
                .multiply(menuProduct.getQuantity()))
            .reduce(Price.ZERO, Price::add);
    }
}

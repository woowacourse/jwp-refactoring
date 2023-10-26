package kitchenpos.domain.menu;

import kitchenpos.domain.menu.menu_product.MenuProduct;
import kitchenpos.domain.menu.menu_product.MenuProducts;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.support.AggregateReference;
import kitchenpos.domain.vo.Price;
import kitchenpos.exception.MenuException;
import kitchenpos.repositroy.MenuGroupRepository;
import kitchenpos.repositroy.ProductRepository;
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
        validatePrice(menu.getMenuPrice(), menu.getMenuProducts());
        validateMenuGroup(menu.getMenuGroup());
    }

    private void validatePrice(final Price menuPrice, final MenuProducts menuProducts) {
        final Price totalMenuProductPrice = menuProducts.menuProducts().stream()
                .map(this::calculateMenuProductPrice)
                .reduce(Price.ZERO, Price::add);

        if (menuPrice.isMoreThan(totalMenuProductPrice)) {
            throw new MenuException.OverPriceException(totalMenuProductPrice);
        }
    }

    private Price calculateMenuProductPrice(final MenuProduct menuProduct) {
        return productRepository.getById(menuProduct.getProductId().getId())
                .getProductPrice().multiply(menuProduct.getQuantity());
    }

    public void validateMenuGroup(final AggregateReference<MenuGroup> menuGroup) {
        if (!menuGroupRepository.existsById(menuGroup.getId())) {
            throw new MenuException.NoMenuGroupException();
        }
    }
}

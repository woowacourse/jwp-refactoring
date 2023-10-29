package domain;

import domain.menu_product.MenuProduct;
import domain.menu_product.MenuProducts;
import exception.MenuException;
import org.springframework.stereotype.Component;
import repository.MenuGroupRepository;
import repository.ProductRepository;
import support.AggregateReference;
import vo.Price;

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

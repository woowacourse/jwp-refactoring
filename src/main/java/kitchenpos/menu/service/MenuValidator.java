package kitchenpos.menu.service;

import kitchenpos.generic.Money;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        if (!hasReasonablePrice(menu)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean hasReasonablePrice(Menu menu) {
        Money price = menu.getPrice();
        MenuProducts menuProducts = menu.getMenuProducts();
        return price.isLessThanOrEqual(menuProducts.calculateTotalPrice());
    }
}

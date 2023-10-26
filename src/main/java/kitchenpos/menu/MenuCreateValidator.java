package kitchenpos.menu;

import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.product.Price;
import org.springframework.stereotype.Component;

@Component
public class MenuCreateValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductPriceMultiplier menuProductPriceMultiplier;

    public MenuCreateValidator(MenuGroupRepository menuGroupRepository, MenuProductPriceMultiplier menuProductPriceMultiplier) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductPriceMultiplier = menuProductPriceMultiplier;
    }

    public void validate(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }

        menu.validateMenuProductsPrice(menuProductPriceMultiplier);

        menu.getMenuProducts().stream()
                .map(menuProductPriceMultiplier::multiply)
                .reduce(Price.zero(), Price::add);
    }
}

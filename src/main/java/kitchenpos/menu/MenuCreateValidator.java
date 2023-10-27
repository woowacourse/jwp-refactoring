package kitchenpos.menu;

import kitchenpos.menugroup.MenuGroupRepository;
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
        if (menuGroupRepository.existsById(menu.getMenuGroupId())) {
            menu.validateMenuProductsPrice(menuProductPriceMultiplier);
            return;
        }
        throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
    }
}

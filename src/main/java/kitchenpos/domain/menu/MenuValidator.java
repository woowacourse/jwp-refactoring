package kitchenpos.domain.menu;

import java.util.Objects;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.support.money.Money;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹 아이디입니다.");
        }
        Money menuPrice = menu.getPrice();
        if (Objects.isNull(menuPrice) || menuPrice.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("메뉴의 가격은 0원 이상이어야 합니다.");
        }
        MenuProducts menuProducts = menu.getMenuProducts();
        if (menuPrice.isGreaterThan(menuProducts.calculateAmount())) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 금액의 합보다 클 수 없습니다.");
        }
    }
}

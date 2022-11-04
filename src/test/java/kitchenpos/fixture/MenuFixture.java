package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.common.vo.Price;
import kitchenpos.menu.dto.MenuRequest;

public class MenuFixture {

    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(name);
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity, BigDecimal menuPrice) {
        return new MenuProduct(productId, quantity, Price.valueOf(menuPrice));
    }

    public static MenuRequest createMenuRequest(String name, BigDecimal price, Long menuGroupId,
                                                MenuProduct... menuProducts) {
        return new MenuRequest(name, price, menuGroupId, List.of(menuProducts));
    }
}

package kitchenpos.test.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.MenuProductMapper;
import kitchenpos.menu.application.dto.MenuProductQuantityDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

public class MenuFixture {

    public static Menu 메뉴(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public static Menu 메뉴(
            String name,
            BigDecimal price,
            MenuGroup menuGroup,
            List<MenuProductQuantityDto> menuProductQuantities,
            MenuProductMapper menuProductMapper
    ) {
        return new Menu(
                name,
                price,
                menuGroup,
                menuProductQuantities,
                menuProductMapper
        );
    }
}

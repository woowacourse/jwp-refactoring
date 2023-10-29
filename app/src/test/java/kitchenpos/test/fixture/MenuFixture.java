package kitchenpos.test.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.MenuProductMapper;
import kitchenpos.application.dto.MenuProductQuantityDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

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

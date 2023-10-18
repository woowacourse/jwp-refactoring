package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_생성(
            BigDecimal price,
            MenuGroup savedMenuGroup,
            MenuProduct menuProduct
    ) {
        return new Menu(
                "메뉴",
                price,
                savedMenuGroup.getId(),
                List.of(menuProduct)
        );
    }

    public static Menu 존재하지_않는_MenuGroup_을_가진_메뉴_생성(
            BigDecimal price,
            MenuProduct savedMenuProduct
    ) {
        return new Menu(
                "메뉴",
                price,
                Long.MAX_VALUE,
                List.of(savedMenuProduct)
        );
    }

}

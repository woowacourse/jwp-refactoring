package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_생성(
            Long price,
            MenuGroup savedMenuGroup,
            MenuProduct menuProduct
    ) {
        return Menu.ofWithMenuProducts(
                "메뉴",
                price,
                savedMenuGroup,
                List.of(menuProduct)
        );
    }

    public static Menu 존재하지_않는_메뉴_그룹을_가진_메뉴_생성(
            Long price,
            MenuProduct savedMenuProduct
    ) {
        return Menu.ofWithMenuProducts(
                "메뉴",
                price,
                MenuGroupFixture.떠오르는_메뉴_그룹(),
                List.of(savedMenuProduct)
        );
    }

}

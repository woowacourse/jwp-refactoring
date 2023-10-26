package kitchenpos.fixture;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_생성(
            Long price,
            MenuGroup savedMenuGroup,
            MenuProduct menuProduct
    ) {
        Menu menu = Menu.of(
                "메뉴",
                price,
                savedMenuGroup.getId()
        );

        menu.addAllMenuProducts(List.of(menuProduct));
        return menu;
    }

    public static Menu 존재하지_않는_메뉴_그룹을_가진_메뉴_생성(
            Long price,
            MenuProduct menuProduct
    ) {
        Menu menu = Menu.of(
                "메뉴",
                price,
                MenuGroupFixture.후추와_함께하는_메뉴().getId()
        );

        menu.addAllMenuProducts(List.of(menuProduct));
        return menu;
    }

}

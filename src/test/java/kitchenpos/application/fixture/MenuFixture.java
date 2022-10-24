package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

public class MenuFixture {

    public static Menu 메뉴_등록(final String name, final BigDecimal price, final Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);

        return menu;
    }

    public static Menu 후라이드_치킨(final MenuGroup menuGroup) {
        Menu menu = new Menu();
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(18000));
        menu.setMenuGroupId(menuGroup.getId());

        return menu;
    }

    public static Menu 양념_치킨(final MenuGroup menuGroup) {
        Menu menu = new Menu();
        menu.setName("양념치킨");
        menu.setPrice(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(menuGroup.getId());

        return menu;
    }

    public static Menu 포테이토_피자(final MenuGroup menuGroup) {
        Menu menu = new Menu();
        menu.setName("포테이토피자");
        menu.setPrice(BigDecimal.valueOf(15000));
        menu.setMenuGroupId(menuGroup.getId());

        return menu;
    }
}

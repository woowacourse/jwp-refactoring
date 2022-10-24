package kitchenpos.fixture;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;

public class MenuFixtures {

    public static Menu createMenu(final String name, final BigDecimal price, final Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    public static Menu 후라이드치킨(final Long menuGroupId) {
        return createMenu("후라이드치킨", BigDecimal.valueOf(16000), menuGroupId);
    }

    public static Menu 양념치킨(final Long menuGroupId) {
        return createMenu("양념치킨", BigDecimal.valueOf(16000), menuGroupId);
    }

    public static Menu 반반치킨(final Long menuGroupId) {
        return createMenu("반반치킨", BigDecimal.valueOf(16000), menuGroupId);
    }

    public static Menu 통구이(final Long menuGroupId) {
        return createMenu("통구이", BigDecimal.valueOf(16000), menuGroupId);
    }

    public static Menu 간장치킨(final Long menuGroupId) {
        return createMenu("간장치킨", BigDecimal.valueOf(17000), menuGroupId);
    }

    public static Menu 순살치킨(final Long menuGroupId) {
        return createMenu("순살치킨", BigDecimal.valueOf(17000), menuGroupId);
    }
}

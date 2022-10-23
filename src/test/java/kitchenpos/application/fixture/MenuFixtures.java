package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;

public class MenuFixtures {

    public static final Menu generateMenu(final String name, final BigDecimal price, final Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    public static final Menu 후라이드치킨(final Long menuGroupId) {
        return generateMenu("후라이드치킨", BigDecimal.valueOf(16000), menuGroupId);
    }

    public static final Menu 양념치킨(final Long menuGroupId) {
        return generateMenu("양념치킨", BigDecimal.valueOf(16000), menuGroupId);
    }

    public static final Menu 반반치킨(final Long menuGroupId) {
        return generateMenu("반반치킨", BigDecimal.valueOf(16000), menuGroupId);
    }

    public static final Menu 통구이(final Long menuGroupId) {
        return generateMenu("통구이", BigDecimal.valueOf(16000), menuGroupId);
    }

    public static final Menu 간장치킨(final Long menuGroupId) {
        return generateMenu("간장치킨", BigDecimal.valueOf(17000), menuGroupId);
    }

    public static final Menu 순살치킨(final Long menuGroupId) {
        return generateMenu("순살치킨", BigDecimal.valueOf(17000), menuGroupId);
    }
}

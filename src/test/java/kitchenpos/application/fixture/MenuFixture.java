package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

public class MenuFixture {

    public static Menu 후라이드_치킨(final MenuGroup menuGroup) {
        return new Menu("후라이드치킨", BigDecimal.valueOf(18000), menuGroup.getId());
    }

    public static Menu 양념_치킨(final MenuGroup menuGroup) {
        return new Menu("양념치킨", BigDecimal.valueOf(19000), menuGroup.getId());
    }

    public static Menu 포테이토_피자(final MenuGroup menuGroup) {
        return new Menu("포테이토피자", BigDecimal.valueOf(15000), menuGroup.getId());
    }
}

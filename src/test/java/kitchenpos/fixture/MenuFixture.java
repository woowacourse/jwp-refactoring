package kitchenpos.fixture;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;

public class MenuFixture {

    public static Menu 메뉴_아메리카노() {
        return new Menu("아메리카노", BigDecimal.valueOf(10000), 1L);
    }
}

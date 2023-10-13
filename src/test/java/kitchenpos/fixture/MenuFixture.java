package kitchenpos.fixture;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;

public class MenuFixture {

    public static Menu 메뉴_생성(String name, BigDecimal price, Long productId) {
        return new Menu(name, price, productId);
    }
}

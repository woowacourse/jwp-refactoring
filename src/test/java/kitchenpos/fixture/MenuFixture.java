package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_망고치킨_17000원_신메뉴() {
        return new Menu("망고 치킨", BigDecimal.valueOf(17000), 1L,
                List.of(MenuProductFixture.메뉴상품_망고_2개(), MenuProductFixture.메뉴상품_치킨_1개()));
    }

    public static Menu 메뉴_망고치킨_N원_신메뉴(final int price) {
        return new Menu("망고 치킨", BigDecimal.valueOf(price), 1L,
                List.of(MenuProductFixture.메뉴상품_망고_2개(), MenuProductFixture.메뉴상품_치킨_1개()));
    }
}

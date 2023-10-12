package kitchenpos.fixture;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_망고치킨_17000원_신메뉴() {
        final var menu = new Menu();
        menu.setId(1L);
        menu.setName("망고 치킨");
        menu.setPrice(BigDecimal.valueOf(17000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(MenuProductFixture.메뉴상품_망고_2개(), MenuProductFixture.메뉴상품_치킨_1개()));
        return menu;
    }
}

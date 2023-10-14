package kitchenpos.domain.fixture;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;

public class MenuFixture {

    public static Menu 치킨_피자_세트_메뉴_생성(final Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName("치킨 피자 세트");
        menu.setPrice(BigDecimal.valueOf(30000));
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }
}

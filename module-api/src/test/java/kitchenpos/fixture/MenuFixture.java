package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_후라이드() {
        MenuGroup 추천메뉴 = MenuGroupFixture.추천메뉴();
        return new Menu(1L, "후라이드", BigDecimal.valueOf(19000), 추천메뉴);
    }
}

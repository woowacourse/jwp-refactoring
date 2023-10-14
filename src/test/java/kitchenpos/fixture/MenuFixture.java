package kitchenpos.fixture;

import static kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT.후라이드_치킨;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    private MenuFixture() {
    }

    public static class REQUEST {

        public static Menu 메뉴_등록_요청() {
            Menu menu = new Menu();
            menu.setId(1L);
            menu.setName("후라이드치킨");
            menu.setPrice(BigDecimal.valueOf(16000L));
            menu.setMenuGroupId(1L);
            menu.setMenuProducts(List.of(후라이드_치킨()));
            return menu;
        }
    }
}

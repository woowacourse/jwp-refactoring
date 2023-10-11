package kitchenpos.fixture;

import static kitchenpos.fixture.MenuFixture.MENU_PRODUCT.후라이드_치킨;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

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

        public static Menu 메뉴_등록_요청(Long price) {
            Menu menu = new Menu();
            menu.setId(1L);
            menu.setName("후라이드치킨");
            menu.setPrice(BigDecimal.valueOf(price));
            menu.setMenuGroupId(1L);
            menu.setMenuProducts(List.of(후라이드_치킨()));
            return menu;
        }
    }

    public static class MENU_PRODUCT {

        public static MenuProduct 후라이드_치킨() {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(1L);
            menuProduct.setQuantity(1);
            return menuProduct;
        }

        public static MenuProduct 후라이드_치킨(Long price) {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(1L);
            menuProduct.setQuantity(1);
            return menuProduct;
        }
    }
}

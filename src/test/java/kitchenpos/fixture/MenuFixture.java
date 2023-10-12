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
            return Menu.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(16000L))
                    .menuGroupId(1L)
                    .menuProducts(List.of(후라이드_치킨()))
                    .build();
        }

        public static Menu 메뉴_등록_요청(Long price) {
            return Menu.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(price == null ? null : BigDecimal.valueOf(price))
                    .menuGroupId(1L)
                    .menuProducts(List.of(후라이드_치킨()))
                    .build();
        }
    }

    public static class MENU_PRODUCT {

        public static MenuProduct 후라이드_치킨() {
            return MenuProduct.builder()
                    .seq(1L)
                    .menuId(1L)
                    .productId(1L)
                    .quantity(1)
                    .build();
        }

        public static MenuProduct 후라이드_치킨(Long quantity) {
            return MenuProduct.builder()
                    .seq(1L)
                    .menuId(1L)
                    .productId(1L)
                    .quantity(quantity)
                    .build();
        }
    }
}

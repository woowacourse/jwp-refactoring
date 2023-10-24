package kitchenpos.fixture;

import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.application.dto.response.CreateMenuResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuProductFixture.MENU_PRODUCT;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    private MenuFixture() {
    }

    public static class REQUEST {

        public static CreateMenuRequest 후라이드_치킨_16000원_1마리_등록_요청() {
            return CreateMenuRequest.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(16000L))
                    .menuGroupId(1L)
                    .menuProducts(List.of(MenuProductFixture.REQUEST.후라이드_치킨_1마리_요청()))
                    .build();
        }

        public static CreateMenuRequest 후라이드_치킨_N원_1마리_등록_요청(Long price) {
            return CreateMenuRequest.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(price == null ? null : BigDecimal.valueOf(price))
                    .menuGroupId(1L)
                    .menuProducts(List.of(MenuProductFixture.REQUEST.후라이드_치킨_1마리_요청()))
                    .build();
        }
    }

    public static class RESPONSE {

        public static CreateMenuResponse 후라이드_치킨_생성_응답() {
            return CreateMenuResponse.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(16000L))
                    .menuGroupId(1L)
                    .menuProducts(List.of())
                    .build();
        }

        public static MenuResponse 후라이드_치킨() {
            return MenuResponse.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(16000L))
                    .menuGroupId(1L)
                    .menuProducts(List.of(MenuProductFixture.RESPONSE.후라이드_치킨_1마리_응답()))
                    .build();
        }
    }

    public static class MENU {

        public static Menu 후라이드_치킨_16000원_1마리() {
            return Menu.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(16000L))
                    .menuGroupId(1L)
                    .menuProducts(List.of(MENU_PRODUCT.후라이드_치킨_1마리()))
                    .build();
        }

        public static Menu 후라이드_치킨_N원_1마리(long price) {
            return Menu.builder()
                    .id(1L)
                    .name("후라이드치킨")
                    .price(BigDecimal.valueOf(price))
                    .menuGroupId(1L)
                    .menuProducts(List.of(MENU_PRODUCT.후라이드_치킨_1마리()))
                    .build();
        }
    }
}

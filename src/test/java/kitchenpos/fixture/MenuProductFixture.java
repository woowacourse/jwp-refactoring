package kitchenpos.fixture;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.response.MenuProductResponse;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static class REQUEST {

        public static MenuProductRequest 후라이드_치킨_1마리_요청() {
            return MenuProductRequest.builder()
                    .seq(1L)
                    .menuId(1L)
                    .productId(1L)
                    .quantity(1)
                    .build();
        }
    }

    public static class RESPONSE {

        public static MenuProductResponse 후라이드_치킨_1마리_응답() {
            return MenuProductResponse.builder()
                    .seq(1L)
                    .menuId(1L)
                    .productId(1L)
                    .quantity(1)
                    .build();
        }
    }

    public static class MENU_PRODUCT {

        public static MenuProduct 후라이드_치킨_1마리() {
            return MenuProduct.builder()
                    .seq(1L)
                    .menuId(1L)
                    .productId(1L)
                    .quantity(1)
                    .build();
        }
    }
}

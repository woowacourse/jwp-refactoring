package kitchenpos.fixture;

import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.response.MenuProductResponse;
import kitchenpos.domain.menu.MenuProduct;

import static kitchenpos.fixture.ProductFixture.PRODUCT;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static class REQUEST {

        public static MenuProductRequest 상품_N_M개_요청(Long productId, int quantity) {
            return MenuProductRequest.builder()
                    .productId(productId)
                    .quantity(quantity)
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
                    .productId(PRODUCT.후라이드_치킨(16000L).getId())
                    .quantity(1)
                    .build();
        }
    }
}

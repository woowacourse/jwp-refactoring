package kitchenpos.fixture;

import kitchenpos.dto.request.MenuProductRequest;

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
}

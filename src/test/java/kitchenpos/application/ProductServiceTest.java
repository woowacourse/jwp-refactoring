package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @DisplayName("특정 메뉴 상품을 추가할 시 메뉴 상품 목록에 추가된다.")
    @Test
    void createAndList() {
        ProductResponse 상품_후라이드 = productService.create(상품요청_후라이드());

        List<ProductResponse> 상품들 = productService.list();

        검증_필드비교_값포함(assertThat(상품들), 상품_후라이드);
    }

    @DisplayName("특정 메뉴 상품들을 추가할 시 메뉴 상품 목록에 추가된다.")
    @Test
    void createAndList_multi() {
        ProductResponse 상품_후라이드 = productService.create(상품요청_후라이드());
        ProductResponse 상품_후라이드2 = productService.create(상품요청_후라이드());

        List<ProductResponse> 상품들 = productService.list();

        검증_필드비교_동일_목록(상품들, List.of(상품_후라이드, 상품_후라이드2));
    }
}

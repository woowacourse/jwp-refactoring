package kitchenpos.ui.product;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductRestControllerTest extends ProductAcceptanceTestUtil {

    @Test
    void 상품_생성() {
        // given
        var 요청 = 상품_생성_요청();

        // when
        var 응답 = 상품을_생성한다(요청);

        // then
        상품이_생성됨(요청, 응답);
    }

    @Test
    void 상품_목록_조회() {
        // given
        var 요청 = 상품_생성_요청();
        상품을_생성한다(요청);

        // when
        var 응답 = 상품_목록을_조회한다();

        // then
        상품이_조회됨(응답);
    }
}

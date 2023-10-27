package com.kitchenpos.ui.product;

import com.kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static com.kitchenpos.fixture.ProductFixture.상품_생성_10000원;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductRestControllerAcceptanceTest extends ProductRestControllerAcceptanceTestFixture {

    private Product 상품;

    @BeforeEach
    void setup() {
        상품 = 상품_생성_10000원();
    }

    @Test
    void 상품을_생성한다() {
        // when
        var 생성_결과 = 상품을_생성한다("/api/products", 상품);

        // then
        상품이_성공적으로_생성된다(생성_결과, 상품);
    }

    @Test
    void 상품을_모두_조회한다() {
        // given
        var 상품_데이터 = 상품_데이터를_생성한다();

        // when
        var 조회_결과 = 상품을_전체_조회한다("/api/products");

        // then
        상품들이_성공적으로_조회된다(조회_결과, 상품_데이터);
    }
}

package kitchenpos.integration;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.request.CreateProductRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.supports.IntegrationTestContext;
import kitchenpos.vo.PriceIsNegativeException;
import kitchenpos.vo.PriceIsNotProvidedException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductIntegrationTest extends IntegrationTestContext {

    @Test
    void 상품_생성_시_가격이_0보다_작으면_예외를_던진다() {
        // given
        CreateProductRequest request = new CreateProductRequest("name", BigDecimal.valueOf(-1L));

        // when, then
        Assertions.assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(PriceIsNegativeException.class);
    }

    @Test
    void 상품_생성_시_가격을_지정하지_않았다면_예외를_던진다() {
        // given
        CreateProductRequest request = new CreateProductRequest("name", null);

        // when, then
        Assertions.assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(PriceIsNotProvidedException.class);
    }

    @Test
    void 상품을_정상적으로_생성하는_경우_생성한_상품이_반환된다() {
        // given
        CreateProductRequest request = new CreateProductRequest("name", BigDecimal.valueOf(1000L));

        // when
        ProductResponse response = productService.create(request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getName()).isEqualTo(request.getName());
        });
    }

    @Test
    void 전체_상품을_조회할_수_있다() {
        // given
        CreateProductRequest request = new CreateProductRequest("name", BigDecimal.valueOf(1000L));
        productService.create(request);

        // when
        List<ProductResponse> response = productService.findAll();

        // then
        Assertions.assertThat(response).hasSize(1);
    }
}

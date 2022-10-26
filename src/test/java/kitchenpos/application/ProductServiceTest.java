package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Test
    void 제품을_저장한다() {
        // given
        final ProductRequest productRequest = new ProductRequest("제품", new BigDecimal(10000));

        // when
        final ProductResponse productResponse = productService.create(productRequest);

        // then
        assertThat(productResponse.getId()).isEqualTo(1L);
    }

    @Test
    void 제품을_저장할_때_가격이_음수이거나_Null이면_예외를_발생한다() {
        // given
        final ProductRequest productRequest = new ProductRequest("제품", new BigDecimal(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 제품을_저장할_때_가격이_null이면_예외를_발생한다() {
        // given
        final ProductRequest productRequest = new ProductRequest("제품", null);

        // when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

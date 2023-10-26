package kitchenpos.product.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductPriceTest {

    @Test
    void 생성한다() {
        // given
        BigDecimal value = BigDecimal.valueOf(1L);

        // expect
        assertThatNoException().isThrownBy(() -> ProductPrice.valueOf(value));
    }

    @Test
    void 생성할_때_상품_가격이_최솟값_미만이면_예외를_던진다() {
        // given
        BigDecimal value = BigDecimal.valueOf(-1L);

        // expect
        assertThatThrownBy(() -> ProductPrice.valueOf(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 0 미만일 수 없습니다.");
    }

    @Test
    void 곱한다() {
        // given
        ProductPrice productPrice = ProductPrice.valueOf(BigDecimal.ONE);

        // when
        ProductPrice result = productPrice.multiply(10L);
        
        // then
        assertThat(result).isEqualTo(ProductPrice.valueOf(BigDecimal.TEN));
    }
}

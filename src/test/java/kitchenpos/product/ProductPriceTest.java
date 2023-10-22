package kitchenpos.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ProductPriceTest {
    @Test
    @DisplayName("가격이 null이거나 음수이면 예외가 발생한다.")
    void validateProductPrice() {
        // given
        final BigDecimal price1 = null;
        final BigDecimal price2 = BigDecimal.valueOf(-1);

        // when, then
        assertSoftly(softly -> {
            assertThatThrownBy(() -> new ProductPrice(price1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품 가격은 0보다 작을 수 없습니다.");
            assertThatThrownBy(() -> new ProductPrice(price2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품 가격은 0보다 작을 수 없습니다.");
        });
    }
}

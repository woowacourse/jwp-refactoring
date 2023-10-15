package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품의 가격이 0원보다 낮으면 예외가 발생한다.")
    @Test
    void create_priceLessThanMinimumValue_ExceptionThrown() {
        // given
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);

        // when, then
        assertThatThrownBy(() -> new Product("함정", invalidPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

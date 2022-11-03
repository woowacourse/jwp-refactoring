package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품의 가격이 비어있으면 예외를 발생")
    @Test
    void priceNull() {
        // given & when & then

        assertThatThrownBy(
                () -> new Product("상품 이름", null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품의 가격은 비어있거나 0보다 작을 수 없습니다.");
    }

    @DisplayName("상품의 가격이 0원보다 작으면 예외를 발생")
    @Test
    void price0() {
        // given & when & then

        assertThatThrownBy(
                () -> new Product("상품 이름", BigDecimal.valueOf(-1))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품의 가격은 비어있거나 0보다 작을 수 없습니다.");
    }

}

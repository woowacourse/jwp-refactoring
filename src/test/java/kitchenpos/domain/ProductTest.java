package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품의 가격이 0보다 작으면 상품을 등록할 수 없다.")
    @Test
    void createWithMinusPrice() {
        assertThatThrownBy(() -> Product.of("후라이드", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 없는 경우 상품을 등록할 수 없다.")
    @Test
    void createWithNullPrice() {
        assertThatThrownBy(() -> Product.of("후라이드", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

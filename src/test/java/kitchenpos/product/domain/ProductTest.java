package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("validateByPrice: price가 null일 경우 예외처리")
    @Test
    void validateByPriceTest1() {
        assertThatThrownBy(() -> new Product("후라이드 치킨", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("validateByPrice: price가 0미만일 경우 예외처리")
    @Test
    void validateByPriceTest2() {
        assertThatThrownBy(() -> new Product("후라이드 치킨", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 상품의 가격은 0 이상이여야 합니다.");
    }
}

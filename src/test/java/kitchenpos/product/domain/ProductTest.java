package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품을 생성할 때 가격이 null이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_null() {
        assertThatThrownBy(() -> Product.builder()
                .name("후라이드")
                .price(null)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 생성할 때 가격이 음수이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_negative() {
        assertThatThrownBy(() -> Product.builder()
                .name("후라이드")
                .price(BigDecimal.valueOf(-1L))
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}

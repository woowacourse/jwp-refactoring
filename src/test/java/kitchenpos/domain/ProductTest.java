package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    private final Long noId = null;

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        // when, then
        assertDoesNotThrow(() -> new Product("후라이드", BigDecimal.valueOf(16000)));
    }

    @Test
    @DisplayName("가격이 음수면 예외를 반환한다.")
    void create_price_negative() {
        // when, then
        assertThatThrownBy(() -> new Product("후라이드", BigDecimal.valueOf(-1)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 null 이면 예외를 반환한다.")
    void create_price_null() {
        // when, then
        assertThatThrownBy(() -> new Product("후라이드", null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

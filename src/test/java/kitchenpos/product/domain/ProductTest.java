package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import kitchenpos.product.model.Product;

class ProductTest {
    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        Product product = new Product(1L, "고추마요 치킨", BigDecimal.valueOf(10_000));

        assertAll(
            () -> assertThat(product.getId()).isNotNull(),
            () -> assertThat(product.getName()).isEqualTo("고추마요 치킨"),
            () -> assertThat(product.getPrice().longValue()).isEqualTo(10_000)
        );
    }

    @DisplayName("상품 등록 시, 상품 가격이 음수 혹은 null이면 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = "-1000")
    @NullSource
    void create_WithNegativePrice_ThrownException(BigDecimal price) {
        assertThatThrownBy(() -> new Product(1L, "고추마요 치킨", price))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
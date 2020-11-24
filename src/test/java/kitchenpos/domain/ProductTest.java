package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {
    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void constructor() {
        Product product = new Product(1L, "상품", BigDecimal.valueOf(0));

        assertAll(
            () -> assertThat(product.getId()).isEqualTo(1L),
            () -> assertThat(product.getName()).isEqualTo("상품"),
            () -> assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(0))
        );
    }

    @DisplayName("상품 가격이 null인 경우 생성할 수 없다.")
    @Test
    void constructor_throws_exception() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Product(1L, "상품", null))
            .withMessage("상품 금액은 0원 이상이어야 합니다.");
    }

    @DisplayName("상품 가격이 0원 미만인 경우 생성할 수 없다.")
    @Test
    void constructor_throws_exception2() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Product(1L, "상품", BigDecimal.valueOf(-1)))
            .withMessage("상품 금액은 0원 이상이어야 합니다.");
    }
}

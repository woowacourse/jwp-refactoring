package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceValidatorTest {
    @DisplayName("제품 목록이 없을 경우 예외 처리한다.")
    @Test
    void ofWithEmptyList() {
        assertThatThrownBy(() -> PriceValidator.of(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 메뉴 제품의 총 가격보다 비싸면 예외 처리한다.")
    @Test
    void validateWithExpensivePrice() {
        PriceValidator priceValidator = PriceValidator.of(Arrays.asList(
            new Product(1L, "test1", BigDecimal.valueOf(1_000L)),
            new Product(2L, "test2", BigDecimal.valueOf(2_000L))));

        assertThatThrownBy(() -> priceValidator.validate(BigDecimal.valueOf(100_000L),
            Arrays.asList(new MenuProductCreateInfo(1L, 1L), new MenuProductCreateInfo(2L, 2L))))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
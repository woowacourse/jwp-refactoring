package kitchenpos.domain.menu.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @Test
    @DisplayName("상품 가격이 존재하지 않으면 예외가 발생한다.")
    void throws_WhenProductPriceNotExist() {
        // when & then
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 메뉴 가격은 null 또는 0 미만의 값일 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, Integer.MIN_VALUE})
    @DisplayName("상품 가격이 0 미만이면 예외가 발생한다.")
    void throws_WhenProductPriceLessThanZero(final int price) {
        // when & then
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 메뉴 가격은 null 또는 0 미만의 값일 수 없습니다.");
    }
}

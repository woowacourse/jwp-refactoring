package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ProductTest {


    @DisplayName("상품을 생성할 떄 가격이 음수이면 예외가 발생한다.")
    @MethodSource
    @ParameterizedTest
    void createFailureWhenPriceIsNegative(BigDecimal price) {

        assertThatThrownBy(
                () -> new Product("짬뽕", price))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 가격은 없거나 음수일 수가 없습니다.");
    }


    private static Stream<BigDecimal> createFailureWhenPriceIsNegative() {
        return Stream.of(null, BigDecimal.valueOf(-1L));
    }

    @DisplayName("상품의 가격과 수량을 곱한다.")
    @Test
    void multiplyPrice() {

        Product product = new Product("짬뽕", BigDecimal.TEN);

        assertThat(product.multiplyPrice(3L)).isEqualTo(BigDecimal.valueOf(30L));
    }
}

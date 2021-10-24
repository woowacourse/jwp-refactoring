package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Product 엔티티 단위 테스트")
class ProductTest {

    @DisplayName("Product는 가격이 null이면 생성 예외가 발생한다.")
    @Test
    void newProduct_PriceNull_Exception() {
        // given, when, then
        assertThatCode(() -> new Product("kevin", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효하지 않은 Product 가격입니다.");
    }

    @DisplayName("Product는 가격이 0 미만인 경우 생성 예외가 발생한다.")
    @Test
    void newProduct_PriceNegative_Exception() {
        // given, when, then
        assertThatCode(() -> new Product("kevin", BigDecimal.valueOf(-1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효하지 않은 Product 가격입니다.");
    }

    @DisplayName("Product는 가격이 0 이상 경우 정상 생성된다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    void newProduct_PriceNegative_Exception(int price) {
        // given, when, then
        assertThatCode(() -> {
            Product product = new Product("kevin", BigDecimal.valueOf(price));
            assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(price));
        }).doesNotThrowAnyException();
    }
}

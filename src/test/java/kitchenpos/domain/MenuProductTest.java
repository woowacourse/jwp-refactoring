package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("MenuProduct 도메인 테스트")
class MenuProductTest {

    private static Stream<Arguments> providePriceAndQuantityAndTotalPrice() {
        return Stream.of(
            Arguments.of(BigDecimal.valueOf(15_000L), 2L, BigDecimal.valueOf(30_000L)),
            Arguments.of(BigDecimal.valueOf(1_000L), 3L, BigDecimal.valueOf(3_000L)),
            Arguments.of(BigDecimal.valueOf(10_000L), 1L, BigDecimal.valueOf(10_000L))
        );
    }

    @DisplayName("[성공] MenuProduct에 포함된 상품들의 값의 합 계산")
    @ParameterizedTest
    @MethodSource({"providePriceAndQuantityAndTotalPrice"})
    void calculatePrice_Success(BigDecimal price, long quantity, BigDecimal expected) {
        // given
        MenuProduct menuProduct = new MenuProduct(new Product("상품", price), quantity);

        // when
        BigDecimal totalPrice = menuProduct.calculatePrice();

        // then
        assertThat(totalPrice).isEqualTo(expected);

    }
}

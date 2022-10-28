package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("메뉴 상품 도메인의")
class MenuProductTest {

    @Nested
    @DisplayName("calculateAmount 메서드는")
    class CalculateAmount {

        @ParameterizedTest
        @DisplayName("상품의 가격과 수량을 곱한 금액을 계산한다.")
        @CsvSource(value = {"1,1000", "4,4000"})
        void calculateAmount_priceExist_success(final Long quantity, final BigDecimal expected) {
            // given
            final MenuProduct menuProduct = new MenuProduct(1L, quantity, BigDecimal.valueOf(1_000L));

            // when
            final BigDecimal actual = menuProduct.calculateAmount();

            // then
            assertThat(actual).isEqualByComparingTo(expected);
        }
    }
}

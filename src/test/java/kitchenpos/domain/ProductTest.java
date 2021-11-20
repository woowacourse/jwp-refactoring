package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidNameException;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Product 단위 테스트")
class ProductTest {

    @DisplayName("Product를 생성할 때")
    @Nested
    class Create {

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // when, then
            assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(5_000)))
                .isExactlyInstanceOf(InvalidNameException.class);
        }

        @DisplayName("name이 공백뿐인 경우 예외가 발생한다.")
        @Test
        void nameBlankException() {
            // when, then
            assertThatThrownBy(() -> new Product(" ", BigDecimal.valueOf(5_000)))
                .isExactlyInstanceOf(InvalidNameException.class);
        }

        @DisplayName("price가 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // when, then
            assertThatThrownBy(() -> new Product("치즈버거", null))
                .isExactlyInstanceOf(InvalidPriceException.class);
        }

        @DisplayName("price가 음수인 경우 예외가 발생한다.")
        @Test
        void priceNegativeException() {
            // when, then
            assertThatThrownBy(() -> new Product("치즈버거", BigDecimal.valueOf(-1)))
                .isExactlyInstanceOf(InvalidPriceException.class);
        }
    }

    @DisplayName("quantity 수 만큼 Product의 가격을 곱해서 반환한다")
    @ParameterizedTest
    @ValueSource(longs = {1, 3, 5})
    void multiply(long value) {
        // given
        Product product = new Product("치즈버거", BigDecimal.valueOf(5_000));

        // when
        Quantity quantity = new Quantity(value);
        Price totalPrice = product.multiplyPrice(quantity);

        // then
        assertThat(totalPrice.getValue()).isEqualTo(product.getPrice().multiply(BigDecimal.valueOf(value)));
    }
}

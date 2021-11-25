package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuQuantity;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Price 단위 테스트")
class PriceTest {

    @DisplayName("Price를 생성할 때")
    @Nested
    class Create {

        @DisplayName("value가 Null일 경우 예외가 발생한다.")
        @Test
        void valueNullException() {
            // when, then
            assertThatThrownBy(() -> new MenuPrice(null))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        }

        @DisplayName("value가 0보다 작을 경우 예외가 발생한다.")
        @Test
        void valueNegativeException() {
            // when, then
            assertThatThrownBy(() -> new MenuPrice(BigDecimal.valueOf(-1)))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        }
    }

    @DisplayName("금액이 더 큰지 비교가 가능하다")
    @Test
    void isBiggerThan() {
        // given
        MenuPrice menuPrice = new MenuPrice(BigDecimal.valueOf(3_000));
        MenuPrice smallerMenuPrice = new MenuPrice(BigDecimal.valueOf(2_000));
        MenuPrice equalMenuPrice = new MenuPrice(BigDecimal.valueOf(3_000));
        MenuPrice biggerMenuPrice = new MenuPrice(BigDecimal.valueOf(5_000));

        // when, then
        assertThat(menuPrice.isBiggerThan(smallerMenuPrice)).isTrue();
        assertThat(menuPrice.isBiggerThan(equalMenuPrice)).isFalse();
        assertThat(menuPrice.isBiggerThan(biggerMenuPrice)).isFalse();
    }

    @DisplayName("주어진 수량에 곱해진 가격의 반환한다.")
    @Test
    void multiplyQuantity() {
        // given
        BigDecimal priceValue = BigDecimal.valueOf(3_000);
        MenuPrice menuPrice = new MenuPrice(priceValue);

        // when
        MenuQuantity menuQuantity = new MenuQuantity(4L);
        MenuPrice result = menuPrice.multiplyQuantity(menuQuantity);

        // then
        assertThat(result.getValue()).isEqualTo(priceValue.multiply(menuQuantity.getDecimalValue()));
    }
}

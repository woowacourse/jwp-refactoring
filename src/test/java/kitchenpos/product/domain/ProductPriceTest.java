package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.domain.MenuQuantity;
import kitchenpos.product.exception.InvalidProductPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductPrice 단위 테스트")
class ProductPriceTest {

    @DisplayName("ProductPrice를 생성할 때")
    @Nested
    class Create {

        @DisplayName("value가 Null일 경우 예외가 발생한다.")
        @Test
        void valueNullException() {
            // when, then
            assertThatThrownBy(() -> new ProductPrice(null))
                .isExactlyInstanceOf(InvalidProductPriceException.class);
        }

        @DisplayName("value가 0보다 작을 경우 예외가 발생한다.")
        @Test
        void valueNegativeException() {
            // when, then
            assertThatThrownBy(() -> new ProductPrice(BigDecimal.valueOf(-1)))
                .isExactlyInstanceOf(InvalidProductPriceException.class);
        }
    }

    @DisplayName("금액이 더 큰지 비교가 가능하다")
    @Test
    void isBiggerThan() {
        // given
        ProductPrice productPrice = new ProductPrice(BigDecimal.valueOf(3_000));
        ProductPrice smallerProductPrice = new ProductPrice(BigDecimal.valueOf(2_000));
        ProductPrice equalProductPrice = new ProductPrice(BigDecimal.valueOf(3_000));
        ProductPrice biggerProductPrice = new ProductPrice(BigDecimal.valueOf(5_000));

        // when, then
        assertThat(productPrice.isBiggerThan(smallerProductPrice)).isTrue();
        assertThat(productPrice.isBiggerThan(equalProductPrice)).isFalse();
        assertThat(productPrice.isBiggerThan(biggerProductPrice)).isFalse();
    }

    @DisplayName("주어진 수량에 곱해진 가격의 반환한다.")
    @Test
    void multiplyQuantity() {
        // given
        BigDecimal priceValue = BigDecimal.valueOf(3_000);
        ProductPrice productPrice = new ProductPrice(priceValue);

        // when
        MenuQuantity menuQuantity = new MenuQuantity(4L);
        ProductPrice result = productPrice.multiplyQuantity(menuQuantity);

        // then
        assertThat(result.getValue()).isEqualTo(priceValue.multiply(menuQuantity.getDecimalValue()));
    }
}

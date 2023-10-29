package product.domain;

import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductTest {

    @DisplayName("상품 생성 테스트")
    @Nested
    class ProductCreateTest {

        @DisplayName("상품을 생성한다.")
        @Test
        void productCreateTest() {
            //given
            final String name = "product";
            final BigDecimal price = new BigDecimal(100);

            //when
            final Product product = new Product(name, price);

            //then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(product.getId()).isNull();
                softly.assertThat(product.getName()).isEqualTo(name);
                softly.assertThat(product.getPrice()).isEqualByComparingTo(price);
            });
        }

        @DisplayName("금액이 음수면 실패한다.")
        @Test
        void productCreateFailWhenPriceLessThenZero() {
            //given
            final String name = "product";
            final BigDecimal price = new BigDecimal(-1);

            // when & then
            Assertions.assertThatThrownBy(() -> new Product(name, price))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("금액이 null이면 실패한다.")
        @Test
        void productCreateFailWhenPriceIsNull() {
            //given
            final String name = "product";
            final BigDecimal price = null;

            // when & then
            Assertions.assertThatThrownBy(() -> new Product(name, price))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

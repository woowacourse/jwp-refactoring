package kitchenpos.domain;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Product의")
class ProductTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {
        @Test
        @DisplayName("가격은 존재해야 한다.")
        void fail_existPrice() {
            Assertions.assertThatThrownBy(() -> new Product(1L, "productA", null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격은 0보다 크거나 같아야 한다.")
        void fail_negativePrice() {
            Assertions.assertThatThrownBy(() -> new Product(1L, "productA", BigDecimal.valueOf(-1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("multiplyPrice 메서드는")
    class MultiplyPrice {

        @Test
        @DisplayName("수량을 받으면, 총 합계 가격을 반환한다.")
        void success() {
            //given
            Product product = new Product(1L, "productA", BigDecimal.valueOf(1000L));

            //when
            BigDecimal total = product.getTotalPrice(10L);

            //then
            Assertions.assertThat(total).isEqualTo(BigDecimal.valueOf(10000L));
        }
    }

}

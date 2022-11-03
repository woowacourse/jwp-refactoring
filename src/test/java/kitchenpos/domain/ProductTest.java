package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

class ProductTest {

    @Nested
    @DisplayName("상품 생성 테스트")
    class product{

        @Test
        @DisplayName("상품을 생성한다.")
        void product(){
            new Product("상품", BigDecimal.valueOf(10000));
        }

        @Test
        @DisplayName("가격이 null일 경우 예외를 반환한다.")
        void product_nullPrice(){
            assertThatThrownBy(() -> new Product("상품", null))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수인 경우 예외를 반환한다.")
        void product_negativePrice(){
            assertThatThrownBy(() -> new Product("상품", BigDecimal.valueOf(-10000)))
                            .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

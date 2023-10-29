package ordertable.test.java.kitchenpos.product.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ProductTest {
    @Nested
    class 상품_생성시 {
        @Test
        void 가격이_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> new Product("치킨", null))
                    .isInstanceOf(InvalidPriceException.class);
        }

        @Test
        void 가격이_음수이면_예외가_발생한다() {
            assertThatThrownBy(() -> new Product("치킨", BigDecimal.valueOf(-1000)))
                    .isInstanceOf(InvalidPriceException.class);
        }
    }
}

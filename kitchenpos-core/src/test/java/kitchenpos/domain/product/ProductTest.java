package kitchenpos.domain.product;

import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void 상품_가격은_0보다_작으면_예외가_발생한다() {
        // given
        int invalidPrice = -1;

        // expect
        assertThatThrownBy(() -> new Product("product", valueOf(invalidPrice)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이여야합니다");
    }
}

package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void validatePrice() {
        assertThatThrownBy(() -> Product.of("name", BigDecimal.valueOf(-10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("product의 가격은 0원 이상이어야 합니다.");
    }
}

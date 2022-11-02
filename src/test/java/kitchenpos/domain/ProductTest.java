package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void validatePrice() {
        assertThatThrownBy(() -> new Product("name", BigDecimal.valueOf(-10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("product의 가격은 0원 이상이어야 합니다.");
    }
}

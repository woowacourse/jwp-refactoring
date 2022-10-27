package kitchenpos.domain;

import static kitchenpos.application.fixture.ProductFixture.PRODUCT_NAME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품의 가격은 0이상이여야 한다. 그렇지 않으면 예외가 발생한다.")
    @Test
    void constructor() {
        assertThatThrownBy(() -> new Product(PRODUCT_NAME, -1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

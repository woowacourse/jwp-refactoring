package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {
    @Test
    @DisplayName("가격이 음수인 상품은 생성할 수 없다")
    void createProduct_negativePriceException() {
        assertThatThrownBy(() -> new Product("이스트상품", BigDecimal.valueOf(-1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

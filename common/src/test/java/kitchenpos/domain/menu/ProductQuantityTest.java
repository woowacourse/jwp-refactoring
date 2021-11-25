package kitchenpos.domain.menu;

import kitchenpos.domain.product.Product;
import kitchenpos.exception.FieldNotValidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductQuantityTest {

    @DisplayName("ProductQuantity를 생성한다. - 실패, product가 null")
    @Test
    void createFailedWithNullProduct() {
        assertThatThrownBy(() -> new ProductQuantity(null, 1L))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("상품이 유효하지 않습니다.");
    }

    @DisplayName("ProductQuantity를 생성한다. - 실패, quantity가 null")
    @Test
    void createFailedWithNullQuantity() {
        assertThatThrownBy(() -> new ProductQuantity(new Product("p", BigDecimal.TEN), null))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("상품 수량이 유효하지 않습니다.");
    }
}

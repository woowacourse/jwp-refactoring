package kitchenpos.domain;

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
                .isInstanceOf(FieldNotValidException.class);
    }

    @DisplayName("ProductQuantity를 생성한다. - 실패, quantity가 null")
    @Test
    void createFailedWithNullQuantity() {
        assertThatThrownBy(() -> new ProductQuantity(new Product("p", BigDecimal.TEN), null))
                .isInstanceOf(FieldNotValidException.class);
    }
}

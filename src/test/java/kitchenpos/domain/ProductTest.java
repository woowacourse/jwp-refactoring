package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @DisplayName("상품의 가격이 Null이면 IllegalArgumentException을 발생시킨다.")
    @Test
    public void validateProduct_nullPrice() {
        Product product = new Product("상품명", null);
        assertThatThrownBy(() -> {
            product.validateProduct();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 음수이면 IllegalArgumentException을 발생시킨다.")
    @Test
    public void validateProduct_negativePrice() {
        Product product = new Product("상품명", new BigDecimal(-100));
        assertThatThrownBy(() -> {
            product.validateProduct();
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

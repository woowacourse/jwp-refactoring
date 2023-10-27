package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품을 생성할 수 있다.")
    void createProduct() {
        // given
        String name = "피자";
        BigDecimal price = BigDecimal.valueOf(20000);

        // when
        Product product = new Product(null, name, price);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("상품 가격은 null이 될 수 없다.")
    void validateNullPrice() {
        // given
        String name = "피자";

        // then
        assertThatThrownBy(() -> new Product(null, name, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격은 음수가 될 수 없다.")
    void validateNegativePrice() {
        // given
        String name = "피자";
        BigDecimal negativePrice = BigDecimal.valueOf(-10000);

        // then
        assertThatThrownBy(() -> new Product(null, name, negativePrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격 계산이 올바르게 작동한다.")
    void calculatePrice() {
        // given
        Product product = new Product(null, "피자", BigDecimal.valueOf(20000));
        long quantity = 3;

        // when
        BigDecimal totalPrice = product.calculatePrice(quantity);

        // then
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(60000));
    }

}

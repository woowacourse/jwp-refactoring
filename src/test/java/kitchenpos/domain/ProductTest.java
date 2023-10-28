package kitchenpos.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @Test
    @DisplayName("상품을 생성할 수 있다.")
    void createProduct() {
        // given
        final String name = "피자";
        final BigDecimal price = BigDecimal.valueOf(20000);

        // when
        final Product product = new Product(null, name, price);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("이름이 비어있으면 예외를 발생시킨다.")
    void throwExceptionIfNameIsEmpty() {
        // given
        final String name = "";
        final BigDecimal price = BigDecimal.valueOf(20000);

        // then
        assertThatThrownBy(
                () -> Product.of(name, price)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이름의 길이가 64를 초과하면 예외를 발생시킨다.")
    void throwExceptionIfNameLengthIsGreaterThan64() {
        // given
        final String name = "a".repeat(65);
        final BigDecimal price = BigDecimal.valueOf(20000);

        // then
        assertThatThrownBy(
                () -> Product.of(name, price)
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("상품 가격은 null이 될 수 없다.")
    void validateNullPrice() {
        // given
        final String name = "피자";

        // then
        assertThatThrownBy(() -> new Product(null, name, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격은 음수가 될 수 없다.")
    void validateNegativePrice() {
        // given
        final String name = "피자";
        final BigDecimal negativePrice = BigDecimal.valueOf(-10000);

        // then
        assertThatThrownBy(
                () -> new Product(null, name, negativePrice)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격 계산이 올바르게 작동한다.")
    void calculatePrice() {
        // given
        final Product product = new Product(null, "피자", BigDecimal.valueOf(20000));
        final long quantity = 3;

        // when
        final BigDecimal totalPrice = product.calculatePrice(quantity);

        // then
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(60000));
    }

}

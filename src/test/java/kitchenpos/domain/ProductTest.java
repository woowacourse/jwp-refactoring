package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("Product 인스턴스를 생성할 수 있어야 함")
    void createProductInstance() {
        //given
        final Long id = 1L;
        final String name = "Test Product";
        final BigDecimal price = BigDecimal.valueOf(10.99);

        //when
        final Product product = new Product(id, name, price);

        //then
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("Product를 생성할 때 유효한 가격을 사용해야 함")
    void createProductWithValidPrice() {
        //given
        final String name = "Test Product";
        final BigDecimal validPrice = BigDecimal.valueOf(10.99);

        //when
        final Product product = Product.of(name, validPrice);

        //then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(validPrice);
    }

    @Test
    @DisplayName("Product를 생성할 때 가격이 null이거나 음수이면 예외를 던져야 함")
    void createProductWithInvalidPrice() {
        //given
        final String name = "Test Product";
        final BigDecimal invalidPrice = BigDecimal.valueOf(-10.99);

        //when
        //then
        assertThatThrownBy(() -> Product.of(name, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Product.of(name, invalidPrice)).isInstanceOf(IllegalArgumentException.class);
    }
}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductTest {

    private Long id = 1L;
    private String name = "pasta";
    private BigDecimal price = BigDecimal.valueOf(13000);

    @Test
    void product를_생성할_수_있다() {
        Product product = new Product(id, name, new Price(price));
        Assertions.assertAll(
                () -> assertThat(product.getId()).isEqualTo(id),
                () -> assertThat(product.getName()).isEqualTo(name),
                () -> assertThat(product.getPrice()).isEqualTo(price)
        );
    }

    @Test
    void 구매_수량을_곱할_수_있다() {
        Product product = new Product(id, name, new Price(price));
        BigDecimal multipliedValue = product.multiply(BigDecimal.valueOf(3));
        assertThat(multipliedValue).isEqualTo(BigDecimal.valueOf(39000));
    }
}

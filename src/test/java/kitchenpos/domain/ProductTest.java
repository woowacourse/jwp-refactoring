package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void create() {
        Product product = new Product("123", new Price(10000));
        assertAll(
                () -> assertThat(product.getName()).isEqualTo("123"),
                () -> assertThat(product.getPriceValue()).isEqualTo(new BigDecimal("10000.0"))
        );
    }
}

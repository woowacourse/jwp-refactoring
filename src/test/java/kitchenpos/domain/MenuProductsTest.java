package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    @Test
    void sum() {
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(
                new MenuProduct(new Product("productA", BigDecimal.TEN), 3L),
                new MenuProduct(new Product("productB", BigDecimal.ONE), 3L)
        ));
        BigDecimal sum = menuProducts.sum();
        assertThat(sum).isEqualTo(BigDecimal.valueOf(33));
    }
}

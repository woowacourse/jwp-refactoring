package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    void getSum() {
        MenuProducts menuProducts = new MenuProducts(new Price(19000),
                List.of(
                        new MenuProduct(1L, 10, new Price(10000)),
                        new MenuProduct(1L, 10, new Price(10000))
                )
        );
        assertThat(menuProducts.getSum()).isEqualTo(new BigDecimal("20000.0"));
    }

    @Test
    void invalidDiscount() {
        assertThatThrownBy(() -> new MenuProducts(new Price(21000),
                List.of(
                        new MenuProduct(1L, 10, new Price(10000)),
                        new MenuProduct(1L, 10, new Price(10000))
                )
        )).isInstanceOf(IllegalArgumentException.class);
    }
}

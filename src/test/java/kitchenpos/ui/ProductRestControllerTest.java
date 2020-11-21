package kitchenpos.ui;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductRestControllerTest extends ControllerTest {

    @Test
    void create() throws Exception {
        Product chicken = product("Chicken", 15000L);
        assertAll(
                () -> assertThat(chicken.getId()).isEqualTo(1L),
                () -> assertThat(chicken.getName()).isEqualTo("Chicken")
        );
    }

    @Test
    void create_negativePrice() {
        assertThatThrownBy(() -> product("Chicken", -10000L))
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() throws Exception {
        // given
        Product chicken = product("Chicken", 15000L);
        Product pizza = product("Pizza", 12000L);

        // when
        List<Product> products = products();

        // then
        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products.get(0)).usingComparatorForType(
                        Comparator.comparingLong(BigDecimal::longValue), BigDecimal.class
                ).isEqualToComparingFieldByField(chicken),
                () -> assertThat(products.get(1)).usingComparatorForType(
                        Comparator.comparingLong(BigDecimal::longValue), BigDecimal.class
                ).isEqualToComparingFieldByField(pizza)
        );
    }
}
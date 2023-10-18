package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class ProductsTest {

    @Test
    void validateSum() {
        //given
        final Product 디노_찌개 = new Product("디노찌개", new BigDecimal(20000));
        final Product 디노_찜 = new Product("디노찜", new BigDecimal(30000));
        final Products products = new Products(List.of(디노_찌개, 디노_찜));

        //when, then
        Assertions.assertThatThrownBy(() -> products.validateSum(List.of(2, 1), new BigDecimal(80000)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductsTest {

    @Test
    void calculateSum() {
        //given
        final Product 디노_찌개 = new Product("디노찌개", new BigDecimal(20000));
        final Product 디노_찜 = new Product("디노찜", new BigDecimal(30000));
        final Products products = new Products(List.of(디노_찌개, 디노_찜));
        final BigDecimal expected = new BigDecimal(70000);

        //when
        final BigDecimal actual = products.calculateSum(List.of(2, 1));

        //then
        assertThat(actual).isEqualTo(expected);
    }
}

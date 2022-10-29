package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void price를_생성할_수_있다() {
        Price price = new Price(BigDecimal.valueOf(13000));
        assertThat(price.getValue()).isEqualTo(BigDecimal.valueOf(13000));
    }

    @Test
    void price가_null이면_예외를_반환한다() {
        BigDecimal nullPrice = null;
        assertThatThrownBy(() -> new Price(nullPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_음수이면_예외를_반환한다() {
        BigDecimal negativePrice = BigDecimal.valueOf(-1000);
        assertThatThrownBy(() -> new Price(negativePrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 구매_총합이_price보다_낮으면_예외를_던진다() {
        Price anotherPrice = new Price(BigDecimal.valueOf(13000));
        Product product = new Product("pasta", anotherPrice);
        Quantity quantity = new Quantity(3L);

        Map<Product, Quantity> rawProductQuantities = new HashMap<>();
        rawProductQuantities.put(product, quantity);
        ProductQuantities productQuantities = new ProductQuantities(rawProductQuantities);

        assertThatThrownBy(() -> Price.ofMenu(BigDecimal.valueOf(40000), productQuantities))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

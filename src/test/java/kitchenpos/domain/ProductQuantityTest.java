package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ProductQuantityTest {

    private Price price = new Price(BigDecimal.valueOf(13000));
    private Product product = new Product("pasta", price);
    private Quantity quantity = new Quantity(3L);


    @Test
    void product_quantity로_총합을_알_수_있다() {
        Map<Product, Quantity> rawProductQuantity = new HashMap<>();
        rawProductQuantity.put(product, quantity);

        ProductQuantity productQuantity = new ProductQuantity(rawProductQuantity);

        assertThat(productQuantity.sum()).isEqualTo(BigDecimal.valueOf(39000));
    }
}

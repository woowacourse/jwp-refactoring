package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("수량에 따른 전체 가격을 구한다")
    void calculateTotalPrice() {
        final Product product = new Product(null, "치킨", new BigDecimal(10_000));
        
        final BigDecimal totalPrice = product.calculateTotalPrice(2);

        assertThat(totalPrice).isEqualByComparingTo(new BigDecimal(20_000));
    }
}

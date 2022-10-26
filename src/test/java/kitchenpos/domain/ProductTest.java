package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("양에 따른 상품 총 가격을 계산한다.")
    @Test
    void calculatePriceByQuantity() {
        //given
        final BigDecimal price = BigDecimal.valueOf(17000);
        Product product = new Product("치킨", price);

        //when
        long quantity = 10;
        BigDecimal actual = product.calculatePriceByQuantity(quantity);

        //then
        BigDecimal expected = price.multiply(BigDecimal.valueOf(quantity));
        assertThat(actual).isEqualTo(expected);
    }
}

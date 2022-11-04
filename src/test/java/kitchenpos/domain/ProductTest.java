package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -10000})
    void construct_가격이_음수이면_예외를_반환한다(int number) {
        BigDecimal incorrectPrice = BigDecimal.valueOf(number);
        assertThatThrownBy(() -> new Product("name", incorrectPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

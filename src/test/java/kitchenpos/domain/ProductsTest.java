package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductsTest {

    @DisplayName("요청온 product들이 없는 product이면 예외를 발생한다.")
    @Test
    void validate_products() {
        // given
        final Products products = new Products(List.of(
            new Product("상품", 1000),
            new Product("상품", 2000)));

        final int wrongRequestProductSize = 1;

        // when
        // then
        assertThatThrownBy(() -> products.validateProductsCnt(wrongRequestProductSize))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품들의 금액 총합을 구한다")
    @Test
    void get_product_total_price() {
        // given
        final Products products = new Products(List.of(
            new Product("상품", 1000),
            new Product("상품", 2000)));

        // when
        final BigDecimal result = products.getTotalPrice();

        // then
        Assertions.assertThat(result).isEqualTo(new BigDecimal(3000));
    }
}

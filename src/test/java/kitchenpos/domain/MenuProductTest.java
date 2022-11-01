package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("제품의 수량에 따른 가격 합을 구한다.")
    void calculateAmount() {
        // given
        final Product product = new Product("product", new Price(1000L));

        // when
        final Price actual = product.calculateAmount(10);

        // then
        assertThat(actual).isEqualTo(new Price(10000L));
    }
}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.vo.Price;
import kitchenpos.vo.Quantity;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 수량만큼의_총_가격을_계산한다() {
        // given
        Quantity quantity = new Quantity(10);
        Product product = new Product("name", new Price(1000));

        // when
        Price result = product.calculateAmount(quantity);

        // then
        assertThat(result).isEqualTo(new Price(1000 * 10));
    }
}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 수량을_받아_값의_총합을_계산한다() {
        // given
        Quantity quantity = new Quantity(10);
        Product product = new Product("후라이드 치킨", new Price(1000));

        // when
        Price result = product.calculateAmount(quantity);

        // then
        assertThat(result).isEqualTo(new Price(10000));
    }
}

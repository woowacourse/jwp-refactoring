package kitchenpos.domain;

import static kitchenpos.support.DomainFixture.뿌링클;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 수량만큼_상품의_가격을_곱한다() {
        // when
        final var multiplied = 뿌링클.multiplyPriceWith(new Quantity(2));

        // then
        assertThat(multiplied).isEqualTo(Price.valueOf(36_000));
    }
}

package kitchenpos.menu.domain;

import static kitchenpos.DomainFixture.두개;
import static kitchenpos.DomainFixture.뿌링클;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 수량만큼_상품의_가격을_곱한다() {
        // when
        final var multiplied = 뿌링클.multiplyPriceWith(두개);

        // then
        assertThat(multiplied).isEqualTo(Price.valueOf(36_000));
    }
}

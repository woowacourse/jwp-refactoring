package kitchenpos.domain;

import static kitchenpos.fixture.DomainFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.PriceException;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 가격이_유효하지_않으면_예외를_발생한다() {
        assertThatThrownBy(() -> new Menu("", BigDecimal.valueOf(-1L), createMenuGroup()))
                .isInstanceOf(PriceException.class);
        assertThatThrownBy(() -> new Menu("", null, createMenuGroup()))
                .isInstanceOf(PriceException.class);
    }
}

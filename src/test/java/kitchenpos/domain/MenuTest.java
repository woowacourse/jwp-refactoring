package kitchenpos.domain;

import static kitchenpos.fixture.DomainFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.exception.PriceException;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 가격이_유효하지_않으면_예외를_발생한다() {
        assertThatThrownBy(() -> new Menu("",
                new Price(BigDecimal.valueOf(-1L)),
                createMenuGroup(),
                new MenuProducts(List.of()))
        ).isInstanceOf(PriceException.class);
    }
}

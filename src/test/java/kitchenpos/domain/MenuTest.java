package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -10000})
    void construct_가격이_음수이면_예외를_반환한다(int number) {
        BigDecimal incorrectPrice = BigDecimal.valueOf(number);
        assertThatThrownBy(() -> new Menu("name", incorrectPrice, 1L, List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

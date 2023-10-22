package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuTest {

    @Test
    void throw_when_price_is_under_or_equal_than_zero() {
        // when & then
        assertThatThrownBy(() -> new Menu("chicken", BigDecimal.valueOf(-1L), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price must be greater than zero.");
    }
}

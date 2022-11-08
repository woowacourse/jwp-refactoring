package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

class PriceTest {

    @ParameterizedTest
    @NullSource
    @CsvSource({"-1"})
    @DisplayName("음수나 Null로 생성할 수 없다.")
    void construct_exceptionNegativeOrNull(final BigDecimal value) {
        // given, when, then
        assertThatThrownBy(() -> new Price(value))
                .isExactlyInstanceOf(InvalidPriceException.class);
    }
}

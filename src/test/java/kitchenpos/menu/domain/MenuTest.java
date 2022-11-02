package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidPriceException;
import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

class MenuTest {

    @ParameterizedTest
    @NullSource
    @CsvSource(value = {"-1"})
    @DisplayName("메뉴 가격이 음수이거나 Null 이면 안된다.")
    void construct_menuPriceNegativeOrNull(final Long priceValue) {
        // given, when, then
        assertThatThrownBy(() -> new Price(priceValue))
                .isExactlyInstanceOf(InvalidPriceException.class);
    }
}

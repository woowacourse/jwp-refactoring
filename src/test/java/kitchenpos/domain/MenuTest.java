package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MenuTest {

    @ParameterizedTest
    @CsvSource(value = {"1900, false", "2000, false", "2100, true"})
    void 메뉴가격과_상품의_총_가격을_비교한다(final int menuPrice, final boolean expected) {
        // given
        final MenuProduct menuProduct = new MenuProduct(null, new Product("야끼만두", new Price(new BigDecimal(1_000))), 2);
        final Menu menu = new Menu("모둠만두", new Price(new BigDecimal(menuPrice)), 1L, List.of(menuProduct));

        // when
        final boolean actual = menu.isPriceGreaterThanMenuProductsPrice();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

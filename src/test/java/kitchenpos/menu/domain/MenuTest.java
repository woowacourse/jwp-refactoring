package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.support.fixture.MenuGroupFixture;
import kitchenpos.support.fixture.MenuProductFixture;
import kitchenpos.support.fixture.ProductFixture;
import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Test
    @DisplayName("메뉴의 가격이 메뉴 제품의 금액 총합보다 크면 안된다.")
    void construct_menuPriceNotBiggerThanAmountSum() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.createWithId(1L);
        final MenuProduct menuProduct = MenuProductFixture.createDefaultWithoutId(
                ProductFixture.createWithPrice(1000L), null);
        final Price price = new Price(BigDecimal.valueOf(1001L));

        // when, then
        assertThatThrownBy(
                () -> new Menu("menu-name", price, menuGroup.getId(), Collections.singletonList(menuProduct)))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
    }
}

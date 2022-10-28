package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴의 가격이 메뉴 제품의 금액 총합보다 크면 안된다.")
    void construct() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.createWithId(1L);
        final MenuProduct menuProduct = MenuProductFixture.createDefaultWithoutId(
                ProductFixture.createWithIdAndPrice(1L, 1000L), null);

        // when, then
        assertThatThrownBy(() -> new Menu(null, "menu-name", BigDecimal.valueOf(1001L), menuGroup.getId(),
                Arrays.asList(menuProduct)))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        // then

    }

}

package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.MenuTotalPriceException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.support.fixtures.MenuFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴의 가격은 메뉴 구성 상품의 총 가격보다 높을 수 없다")
    void validateMenuProductSumPrice() {
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO
                .createWithPrice(new BigDecimal(30_001));

        assertThatThrownBy(() -> menu.validatePrice(new BigDecimal(30_000)))
                .isExactlyInstanceOf(MenuTotalPriceException.class);
    }
}

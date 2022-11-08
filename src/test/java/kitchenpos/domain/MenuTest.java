package kitchenpos.domain;

import static kitchenpos.domain.DomainFixture.MENU_NAME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.exception.InvalidMenuException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @DisplayName("메뉴를 생성할 때")
    @Nested
    class CreateMenu {

        @DisplayName("메뉴 금액이 0보다 작으면 생성할 수 없다.")
        @ParameterizedTest
        @ValueSource(longs = {-1, -20})
        void createMenuWithZeroPrice(long price) {
            assertThatThrownBy(() -> Menu.create(MENU_NAME, BigDecimal.valueOf(price), 1L, List.of()))
                    .isInstanceOf(InvalidMenuException.class)
                    .hasMessage("메뉴 금액이 잘못됐습니다.");
        }

        @DisplayName("메뉴 금액이 상품 금액의 합보다 크면 생성할 수 없다.")
        @Test
        void createMenuOverProductPriceSum() {
            MenuProduct menuProduct1 = new MenuProduct(1L, 2, BigDecimal.valueOf(2_000));
            MenuProduct menuProduct2 = new MenuProduct(2L, 3, BigDecimal.valueOf(3_000));
            assertThatThrownBy(() -> Menu.create(MENU_NAME, BigDecimal.valueOf(2_000 * 2 + 3_000 * 3 + 1), 1L,
                    List.of(menuProduct1, menuProduct2)))
                    .isInstanceOf(InvalidMenuException.class)
                    .hasMessage("메뉴 금액이 상품 금액의 합보다 큽니다.");
        }
    }
}

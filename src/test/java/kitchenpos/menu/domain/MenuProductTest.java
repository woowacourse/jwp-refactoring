package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.math.BigDecimal;
import kitchenpos.menu.exception.InvalidMenuException;
import kitchenpos.menu.exception.InvalidMenuQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MenuProduct 단위 테스트")
class MenuProductTest {

    private static final Long MENU_GROUP_ID = 1L;
    private static final Long PRODUCT_ID = 1L;

    @DisplayName("MenuProduct가 생성될 때")
    @Nested
    class Create {

        @DisplayName("Menu가 null이면 예외가 발생한다.")
        @Test
        void menuNullException() {
            // when, then
            assertThatCode(() -> new MenuProduct(null, PRODUCT_ID, 1L))
                .isExactlyInstanceOf(InvalidMenuException.class);
        }

        @DisplayName("Product가 null이면 예외가 발생한다.")
        @Test
        void productNullException() {
            // given
            Menu menu = new Menu("스테커2 버거 단품 메뉴", BigDecimal.valueOf(11_900), MENU_GROUP_ID);

            // when, then
            assertThatCode(() -> new MenuProduct(menu, null, 1L))
                .isExactlyInstanceOf(InvalidMenuException.class);
        }

        @DisplayName("Quantity가 null이면 예외가 발생한다.")
        @Test
        void quantityNullException() {
            // given
            Menu menu = new Menu("스테커2 버거 단품 메뉴", BigDecimal.valueOf(11_900), MENU_GROUP_ID);

            // when, then
            assertThatCode(() -> new MenuProduct(menu, PRODUCT_ID, null))
                .isExactlyInstanceOf(InvalidMenuQuantityException.class);
        }

        @DisplayName("Quantity가 음수면 예외가 발생한다.")
        @Test
        void quantityNegativeException() {
            // given
            Menu menu = new Menu("스테커2 버거 단품 메뉴", BigDecimal.valueOf(11_900), MENU_GROUP_ID);

            // when, then
            assertThatCode(() -> new MenuProduct(menu, PRODUCT_ID, -1L))
                .isExactlyInstanceOf(InvalidMenuQuantityException.class);
        }
    }
}

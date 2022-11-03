package kitchenpos.menu.domain;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MenuTest {

    private MenuValidator menuValidator;

    @BeforeEach
    void setUp() {
        menuValidator = Mockito.mock(MenuValidator.class);
    }

    @Nested
    @DisplayName("메뉴를 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("메뉴 그룹이 없으면 예외가 발생한다.")
        void noMenuGroupFailed() {
            List<MenuProduct> menuProducts = List.of(new MenuProduct(null, 1L, 1));
            doThrow(IllegalArgumentException.class).when(menuValidator)
                    .validate(eq(null), anyList(), any(BigDecimal.class));

            assertThatThrownBy(() -> Menu.create(MENU1_NAME, MENU1_PRICE, null, menuProducts, menuValidator))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("프로덕트가 없으면 예외가 발생한다.")
        void noProductFailed() {
            doThrow(IllegalArgumentException.class).when(menuValidator)
                    .validate(anyLong(), eq(Collections.emptyList()), any(BigDecimal.class));

            assertThatThrownBy(() -> Menu.create(MENU1_NAME, MENU1_PRICE, 1L, Collections.emptyList(), menuValidator))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 총합보다 크면 예외가 발생한다.")
        void priceMoreExpensiveThanTotalFailed() {
            List<MenuProduct> menuProducts = List.of(new MenuProduct(null, 1L, 1));
            doThrow(IllegalArgumentException.class).when(menuValidator)
                    .validate(anyLong(), anyList(), eq(BigDecimal.ZERO));

            assertThatThrownBy(() -> Menu.create(MENU1_NAME, BigDecimal.ZERO, 1L, menuProducts, menuValidator))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴를 생성한다.")
        void createMenu() {
            List<MenuProduct> menuProducts = List.of(new MenuProduct(null, 1L, 1));
            doNothing().when(menuValidator).validate(anyLong(), anyList(), eq(BigDecimal.ZERO));
            assertDoesNotThrow(() -> Menu.create(MENU1_NAME, BigDecimal.ZERO, 1L, menuProducts, menuValidator));
        }
    }
}

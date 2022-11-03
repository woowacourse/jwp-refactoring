package kitchenpos.domain.menu;

import static kitchenpos.fixture.Fixture.PRODUCT_양념치킨;
import static kitchenpos.fixture.Fixture.PRODUCT_후라이드;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuTest {

    private MenuValidator menuValidator;

    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        menuValidator = mock(MenuValidator.class);
        final MenuProduct menuProduct1 = new MenuProduct(PRODUCT_후라이드.getId(), 1L);
        final MenuProduct menuProduct2 = new MenuProduct(PRODUCT_양념치킨.getId(), 1L);
    }

    @Test
    void Menu_생성_시_MenuGroup이_존재하지_않으면_예외가_발생한다() {
        doThrow(IllegalArgumentException.class).when(menuValidator)
                .validate(eq(null), any(), any());

        assertThatThrownBy(
                () -> Menu.create("후라이드+양념치킨", BigDecimal.valueOf(34000), null, List.of(menuProduct1, menuProduct2),
                        menuValidator)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Menu_생성_시_Product가_존재하지_않으면_예외가_발생한다() {
        doThrow(IllegalArgumentException.class).when(menuValidator)
                .validate(anyLong(), any(), any());
    }

    @Test
    void Menu_생성_시_Menu_가격이_총금액보다_비싸면_예외가_발생한다() {
        doThrow(IllegalArgumentException.class).when(menuValidator)
                .validate(anyLong(), anyList(), eq(BigDecimal.valueOf(34000)));

        assertThatThrownBy(() -> Menu.create("후라이드+양념치킨", BigDecimal.valueOf(34000), 2L,
                List.of(menuProduct1, menuProduct2), menuValidator))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

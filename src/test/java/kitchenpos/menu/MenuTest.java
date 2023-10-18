package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.InvalidMenuException;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴(Menu) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuTest {

    private final MenuValidator menuValidator = mock(MenuValidator.class);

    @Nested
    class 생성_시 {

        @Test
        void 검증시_오류가_존재하면_생성되지_않는다() {
            // given
            willThrow(new InvalidMenuException())
                    .given(menuValidator)
                    .validateCreate(any(), any(), any());

            // when & then
            assertThatThrownBy(() -> menuValidator.validateCreate(
                    BigDecimal.valueOf(1000),
                    null,
                    List.of(new MenuProduct(1L, 2L))
            )).isInstanceOf(InvalidMenuException.class);
        }

        @Test
        void 검증시_오류가_없으면_생성된다() {
            // given
            willDoNothing()
                    .given(menuValidator)
                    .validateCreate(any(), any(), any());

            // when & then
            assertDoesNotThrow(() -> menuValidator.validateCreate(
                    BigDecimal.valueOf(1000),
                    new MenuGroup("말랑"),
                    List.of(new MenuProduct(1L, 2L))
            ));
        }
    }
}

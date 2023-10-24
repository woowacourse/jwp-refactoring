package kitchenpos.domain.menu;

import kitchenpos.BaseTest;
import kitchenpos.exception.MenuProductQuantityException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MenuProductQuantityTest extends BaseTest {

    @Test
    void 상품_수량을_생성한다() {
        // given
        Long quantity = 10L;

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new MenuProductQuantity(quantity));
    }

    @Test
    void 상품_수량을_생성할_때_수량이_null_이면_예외를_던진다() {
        // given
        Long quantity = null;

        // when, then
        Assertions.assertThatThrownBy(() -> new MenuProductQuantity(quantity))
                .isInstanceOf(MenuProductQuantityException.class);
    }

    @Test
    void 상품_수량을_생성할_때_수량이_음수_이면_예외를_던진다() {
        // given
        Long quantity = -10L;

        // when, then
        Assertions.assertThatThrownBy(() -> new MenuProductQuantity(quantity))
                .isInstanceOf(MenuProductQuantityException.class);
    }
}

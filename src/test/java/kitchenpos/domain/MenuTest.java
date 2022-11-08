package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.IgnoreDisplayNameUnderscores;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@IgnoreDisplayNameUnderscores
class MenuTest {

    @Test
    void create_정적_팩토리_메소드는_메뉴_가격이_상품_가격의_합보다_크면_예외가_발생한다() {
        // given
        MenuProduct menuProduct1 = MenuProduct.createWithPrice(null, 1L, BigDecimal.valueOf(5000), 3L);
        MenuProduct menuProduct2 = MenuProduct.createWithPrice(null, 2L, BigDecimal.valueOf(4000), 1L);

        // when & then
        Assertions.assertThatThrownBy(
                        () -> Menu.create("메뉴", BigDecimal.valueOf(20000), 1L, List.of(menuProduct1, menuProduct2)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

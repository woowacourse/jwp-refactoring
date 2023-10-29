package kitchenpos.domain.menu;

import kitchenpos.common.Price;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProducts;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴_가격이_null이면_생성할_수_없다() {
        // given
        final Price nullPrice = null;

        // when, then
        assertThatThrownBy(() -> new Menu("우동세트", nullPrice, new MenuGroup(), new MenuProducts()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_없으면_메뉴를_생성할_수_없다() {
        // when, then
        assertThatThrownBy(() -> new Menu("우동세트", new Price(BigDecimal.valueOf(9000)), null, new MenuProducts()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

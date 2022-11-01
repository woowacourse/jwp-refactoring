package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.common.Price;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴를_생성하면_메뉴_상품에_메뉴를_매핑한다() {
        MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1, new Price(BigDecimal.ZERO));

        Menu menu = new Menu(null, "메뉴", new Price(BigDecimal.ZERO), 1L, List.of(menuProduct));

        assertThat(menuProduct.getMenu()).isEqualTo(menu);
    }

    @Test
    void 가격이_메뉴_상품_가격의_총합보다_비싸면_예외를_반환한다() {
        MenuProduct menuProduct = new MenuProduct(null, null, 1L, 1, new Price(BigDecimal.ZERO));
        assertThatThrownBy(() -> new Menu(null, "메뉴", new Price(BigDecimal.ONE), 1L, List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

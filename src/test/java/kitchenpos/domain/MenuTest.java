package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴의_가격이_음수면_예외발생() {
        assertThatThrownBy(() -> new Menu("menu", BigDecimal.valueOf(-1), new MenuGroup("한식"), Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴에_가격_정보가_없으면_예외발생() {
        assertThatThrownBy(() -> new Menu("menu", null, new MenuGroup("한식"), Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_메뉴상품들의_합보다_크면_예외_발생() {
        Product product = new Product("pizza", BigDecimal.valueOf(100));
        assertThatThrownBy(() -> new Menu(
                "menu",
                BigDecimal.valueOf(1000000),
                new MenuGroup("한식"),
                List.of(new MenuProduct(product, 3)))
        )
                .isInstanceOf(IllegalArgumentException.class);
    }
}

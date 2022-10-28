package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    private static final long MENU_ID = 1L;
    private static final long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;

    @DisplayName("메뉴의 가격은 음수일 수 없다.")
    @Test
    void createWithMinusPrice() {
        assertThatThrownBy(() -> Menu.of("후라이드치킨", BigDecimal.valueOf(-1), 2L, List.of(createMenuProduct())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 반드시 함께 등록되어야 한다.")
    @Test
    void createWithNullPrice() {
        assertThatThrownBy(() -> Menu.of("후라이드치킨", null, 2L, List.of(createMenuProduct())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private MenuProduct createMenuProduct() {
        return new MenuProduct(MENU_ID, PRODUCT_ID, QUANTITY);
    }
}

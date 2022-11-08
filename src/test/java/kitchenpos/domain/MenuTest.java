package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @Test
    @DisplayName("메뉴의 가격은 0원 미만이면 예외를 던진다.")
    void price_underZero_throwException() {
        // when & then
        assertThatThrownBy(
                () -> new Menu("닭강정", BigDecimal.valueOf(-1L), 1L, List.of(new MenuProduct(null, null, 1L, 1L))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품들의 가격 총합보다 클 경우 예외를 던진다.")
    void price_overProductsPrice_throwException() {
        // when & then
        assertThatThrownBy(() -> new Menu("닭강정", BigDecimal.valueOf(10_000L), 1L,
                List.of(new MenuProduct(1L, 1L, 1L, BigDecimal.valueOf(3_000L), 3L))));
    }

    @Test
    @DisplayName("메뉴 그룹에 속해있지 않으면 예외를 던진다.")
    void menuGroup_null_throwException() {
        // when & then
        assertThatThrownBy(() -> new Menu("닭강정", BigDecimal.valueOf(10_000L), null,
                List.of(new MenuProduct(1L, 1L, 1L, BigDecimal.valueOf(3_000L), 3L))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

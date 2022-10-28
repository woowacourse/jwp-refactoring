package kitchenpos.domain;

import static kitchenpos.fixture.MenuProductFixture.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴를 생성할 때 가격이 null이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_null() {
        assertThatThrownBy(() -> new Menu("후라이드+후라이드", null, 1L,
                Collections.singletonList(createMenuProduct(1L, 2))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 때 가격이 음수이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_negative() {
        assertThatThrownBy(() -> new Menu("후라이드+후라이드", BigDecimal.valueOf(-1L), 1L,
                Collections.singletonList(createMenuProduct(1L, 2))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

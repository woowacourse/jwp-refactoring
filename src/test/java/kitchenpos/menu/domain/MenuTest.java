package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 생성_시_가격이_null인_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Menu("후라이드치킨", null, 1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양의 정수만 들어올 수 있습니다.");
    }

    @Test
    void 생성_시_가격이_음수인_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Menu("후라이드치킨", BigDecimal.valueOf(-1), 1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양의 정수만 들어올 수 있습니다.");
    }

    @Test
    void 생성_시_가격총합이_작은경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Menu("후라이드", BigDecimal.valueOf(1000L), 1L,
                Collections.singletonList(new MenuProduct(1L, 1, BigDecimal.valueOf(10L)))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 상품총합보다 작을 수 없습니다.");
    }
}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.Fixtures;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴의 가격이 0보다 작을 경우 예외 발생")
    @Test
    void menuCreate() {
        assertThatThrownBy(() -> {
            new Menu("후라이드치킨", BigDecimal.valueOf(-16000.00), Fixtures.makeMenuGroup().getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

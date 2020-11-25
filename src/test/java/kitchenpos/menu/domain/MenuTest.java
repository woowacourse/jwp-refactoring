package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("메뉴는 가격이 0원 이상이어야 한다.")
    @Test
    void createMenuUnderZeroPrice() {
        //given
        long minusPrice = -1L;

        //then
        assertThatThrownBy(() -> new Menu("간장+허니", minusPrice, new MenuGroup("치킨")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%f : 가격은 0원 이상이어야 합니다.", BigDecimal.valueOf(minusPrice));
    }
}
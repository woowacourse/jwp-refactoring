package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("메뉴는 메뉴상품 목록이 존재해야 한다.")
    @Test
    void createMenuWithoutMenuProducts() {
        //given
        List<MenuProduct> emptyMenuProducts = Collections.emptyList();

        //then
        assertThatThrownBy(() -> new Menu("간장+허니", 60000L, 1L, emptyMenuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴에 등록한 상품이 없습니다.");
    }

    @DisplayName("메뉴는 가격이 0원 이상이어야 한다.")
    @Test
    void createMenuUnderZeroPrice() {
        //given
        long minusPrice = -1L;

        //then
        assertThatThrownBy(() -> new Menu("간장+허니", minusPrice, 1L, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%f : 가격은 0원 이상이어야 합니다.", BigDecimal.valueOf(minusPrice));
    }
}
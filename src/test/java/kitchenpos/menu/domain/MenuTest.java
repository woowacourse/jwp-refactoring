package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;

public class MenuTest {

    @Test
    @DisplayName("가격은 비어있을 수 없다")
    void nullPrice(){
        // given

        // when, then
        assertThatThrownBy(() -> new Menu("test", null, null, List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격은 비어있을 수 없다")
    void minusPrice(){
        // given

        // when, then
        assertThatThrownBy(() -> new Menu("test", BigDecimal.valueOf(-100), null, List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격은 구성 상품 가격의 총합을 초과할 수 없다")
    void priceOverProductSum(){
        // given

        // when, then
        assertThatThrownBy(() -> new Menu("test", BigDecimal.ONE, null, List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

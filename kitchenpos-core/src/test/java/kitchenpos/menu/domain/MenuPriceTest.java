package kitchenpos.menu.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuPriceTest {

    @Test
    void price가_null이면_예외가_발생한다() {
        //when, then
        assertThatThrownBy(() -> new MenuPrice(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 0보다 작거나 null일 수 없습니다.");
    }

    @Test
    void price가_0원보다_작으면_예외가_발생한다() {
        //when, then
        assertThatThrownBy(() -> new MenuPrice(new BigDecimal(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 0보다 작거나 null일 수 없습니다.");
    }

}

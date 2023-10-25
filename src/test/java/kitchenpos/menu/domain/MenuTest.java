package kitchenpos.menu.domain;

import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MenuTest {

    @Test
    void Menu를_생성할_수_있다() {
        //when, then
        assertDoesNotThrow(() -> new Menu("디노 세트", new BigDecimal(8000), null));
    }

    @Test
    void price가_null이면_예외가_발생한다() {
        //when, then
        assertThatThrownBy(() -> new Menu("디노 세트", null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 0보다 작거나 null일 수 없습니다.");
    }

    @Test
    void price가_0원보다_작으면_예외가_발생한다() {
        //when, then
        assertThatThrownBy(() -> new Menu("디노 세트", new BigDecimal(-1), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 0보다 작거나 null일 수 없습니다.");
    }

}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("가격이 null이면 예외를 발생시킨다.")
    void NullPriceError() {
        assertThatThrownBy(() -> new Menu("name", null, new MenuGroup(), Arrays.asList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격은 비어있거나 음수일 수 없습니다.");
    }

    @Test
    @DisplayName("현재 Menu의 가격을 비교한다.")
    void checkPrice() {
        Menu menu = new Menu("name", BigDecimal.valueOf(1000), new MenuGroup(), Arrays.asList());

        assertAll(
            () -> assertThat(menu.checkPrice(BigDecimal.valueOf(1001))).isFalse(),
            () -> assertThat(menu.checkPrice(BigDecimal.valueOf(999))).isTrue()
        );
    }
}
package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuTest {

    @Test
    @DisplayName("예외사항이 존재하지 않는 경우 객체를 생성한다.")
    void menu() {
        assertDoesNotThrow(() -> new Menu("name", new BigDecimal(1000), 1L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("name이 비어있을 경우 예외가 발생한다.")
    void emptyName(String name) {
        assertThatThrownBy(() -> new Menu(name, new BigDecimal(1000), 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 이름은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("price가 null일 경우 예외가 발생한다.")
    void nullPrice() {
        assertThatThrownBy(() -> new Menu("name", null, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 가격은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("price가 0원 미만일 경우 예외가 발생한다.")
    void negativePrice() {
        assertThatThrownBy(() -> new Menu("name", new BigDecimal(-1), 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 0원 미만일 수 없습니다.");
    }

}

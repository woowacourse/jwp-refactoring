package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import kitchenpos.menu.model.Menu;

class MenuTest {
    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() {
        Menu saved = new Menu(1L, "핫후라이드치킨", BigDecimal.valueOf(18_000), 1L);

        assertAll(
            () -> assertThat(saved.getId()).isEqualTo(1L),
            () -> assertThat(saved.getName()).isEqualTo("핫후라이드치킨"),
            () -> assertThat(saved.getPrice().longValue()).isEqualTo(18_000),
            () -> assertThat(saved.getMenuGroupId()).isEqualTo(1L)
        );
    }

    @DisplayName("메뉴 등록 시, 가격이 null 혹은 음수면 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = "-1000")
    @NullSource
    void create_WithInvalidPrice_ThrownException(BigDecimal price) {
        assertThatThrownBy(() -> new Menu(1L, "핫후라이드치킨", price, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴의 가격은 양수여야 합니다.");
    }
}
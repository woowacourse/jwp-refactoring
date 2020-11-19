package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        assertThat(new Menu("test", BigDecimal.valueOf(1_000L), 1L, Collections.emptyList())).isNotNull();
    }

    @DisplayName("메뉴를 생성할 시 가격이 null일 경우 예외 처리한다.")
    @Test
    void createWithNullPrice() {
        assertThatThrownBy(() -> new Menu("test", null, 1L, Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 시 가격이 음수일 경우 예외 처리한다.")
    @Test
    void createWithNegativePrice() {
        assertThatThrownBy(() -> new Menu("test", BigDecimal.valueOf(-10L), 1L, Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
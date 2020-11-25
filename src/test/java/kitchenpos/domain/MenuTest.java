package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {
    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void constructor() {
        Menu menu = new Menu(1L, "메뉴", BigDecimal.valueOf(1_000), 1L);

        assertAll(
            () -> assertThat(menu.getId()).isEqualTo(1L),
            () -> assertThat(menu.getName()).isEqualTo("메뉴"),
            () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(1_000)),
            () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L)
        );
    }

    @DisplayName("메뉴 가격이 null인 경우 생성할 수 없다.")
    @Test
    void constructor_throws_exception() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Menu(1L, "메뉴", null, 1L))
            .withMessage("메뉴 가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("메뉴 가격이 0원 미만인 경우 생성할 수 없다.")
    @Test
    void constructor_throws_exception2() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Menu(1L, "메뉴", BigDecimal.valueOf(-1), 1L))
            .withMessage("메뉴 가격은 0원 이상이어야 합니다.");
    }
}

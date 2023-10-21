package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @DisplayName("메뉴 생성 시, 이름이 비어있으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "  "})
    void menu_FailWithBlankName(String blankName) {
        // when & then
        assertThatThrownBy(() -> Menu.create(blankName, BigDecimal.ZERO, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴명이 비어있습니다.");
    }

    @DisplayName("메뉴 생성 시, 가격이 비어있으면 예외가 발생한다.")
    @Test
    void menu_FailWithNullPrice() {
        // when & then
        assertThatThrownBy(() -> Menu.create("메뉴", null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격이 비어있습니다.");
    }

    @DisplayName("메뉴 생성 시, 가격이 0원 미이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L})
    void menu_FailWithBlankName(Long invalidPrice) {
        // when & then
        assertThatThrownBy(() -> Menu.create("메뉴명", BigDecimal.valueOf(invalidPrice), 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void menu() {
        // then
        assertDoesNotThrow(() -> Menu.create("메뉴명", BigDecimal.ZERO, 1L));
    }
}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @DisplayName("메뉴 생성")
    @Test
    void createMenu() {
        Menu menu = new Menu("콜라", BigDecimal.valueOf(3000L), new MenuGroup("탄산음료"));
        assertAll(
            () -> assertThat(menu).isNotNull(),
            () -> assertThat(menu.getName()).isEqualTo("콜라"),
            () -> assertThat(menu.getMenuGroup().getName()).isEqualTo("탄산음료")
        );
    }

    @DisplayName("메뉴 생성 실패 - 메뉴 이름 없음")
    @ParameterizedTest
    @NullAndEmptySource
    void createFail_Name_isEmpty(String name) {
        assertThatThrownBy(() -> new Menu(name, BigDecimal.valueOf(3000L), null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴 이름을 입력해주세요");
    }

    @DisplayName("메뉴 생성 실패 - 유효하지 않은 가격")
    @ParameterizedTest
    @ValueSource(doubles = {-1L, -1000L})
    void createFail_InvalidPrice(double price) {
        assertThatThrownBy(() -> new Menu("콜라", BigDecimal.valueOf(price), null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효한 가격이 아닙니다.");
    }
}

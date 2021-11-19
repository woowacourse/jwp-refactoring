package kitchenpos.menu;

import kitchenpos.exception.FieldNotValidException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {
    @DisplayName("메뉴를 생성한다. - 실패, name이 ")
    @ParameterizedTest(name = "{displayName} {0}인 경우")
    @NullAndEmptySource
    void createWithNotValidName(String name) {
        // given - when
        assertThatThrownBy(() -> new Menu(name, BigDecimal.TEN))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("메뉴명이 유효하지 않습니다.");
    }

    @DisplayName("메뉴를 생성한다. - 실패, 가격이 음수")
    @Test
    void createWithNotValidPrice() {
        // given - when
        assertThatThrownBy(() -> new Menu("joanne", BigDecimal.valueOf(-10)))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("메뉴의 가격이 유효하지 않습니다.");
    }

    @DisplayName("메뉴를 생성한다. - 실패, 가격이 null")
    @Test
    void createWithNullPrice() {
        // given - when
        assertThatThrownBy(() -> new Menu("joanne", null))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("메뉴의 가격이 유효하지 않습니다.");
    }

    @DisplayName("메뉴를 생성한다. - 실패, MenuGroup이 null")
    @Test
    void createWithNotValidMenuGroup() {
        // given - when
        assertThatThrownBy(() -> new Menu("joanne", BigDecimal.TEN, null))
                .isInstanceOf(FieldNotValidException.class)
                .hasMessageContaining("메뉴 그룹이 유효하지 않습니다.");
    }

    @DisplayName("메뉴 가격과 상품 가격 합산을 검증한다. - 실패")
    @Test
    void validateTotalPrice() {
        // given - when
        Menu menu = new Menu("joanne", BigDecimal.TEN, new MenuGroup("group"));
        assertThatThrownBy(() -> menu.validateTotalPrice(BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품 가격에 비해 메뉴의 가격이 큽니다.");
    }
}

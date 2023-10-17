package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    @DisplayName("메뉴 이름이 존재하지 않으면 예외가 발생한다")
    void create_fail1() {
        //given
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 5));

        //when, then
        assertThatThrownBy(() -> new Menu(" ", BigDecimal.TEN, 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 이름이 필요합니다.");
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면 예외가 발생한다")
    void create_fail2() {
        //given
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 5));

        //when, then
        assertThatThrownBy(() -> new Menu("한식 세트", BigDecimal.TEN, null, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 필요합니다.");
    }

    @Test
    @DisplayName("메뉴 상품이 존재하지 않으면 예외가 발생한다")
    void create_fail3() {
        //given
        final List<MenuProduct> menuProducts = emptyList();

        //when, then
        assertThatThrownBy(() -> new Menu("한식 세트", BigDecimal.TEN, 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 상품이 필요합니다.");
    }

    @Test
    @DisplayName("메뉴 금액이 존재하지 않으면 예외가 발생한다")
    void create_fail4() {
        //given
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 5));

        //when, then
        assertThatThrownBy(() -> new Menu("한식 세트", null, 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금액이 없거나 음수입니다.");
    }

    @Test
    @DisplayName("메뉴 금액이 음수이면 예외가 발생한다")
    void create_fail5() {
        //given
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 5));

        //when, then
        assertThatThrownBy(() -> new Menu("한식 세트", BigDecimal.valueOf(-100), 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금액이 없거나 음수입니다.");
    }
}

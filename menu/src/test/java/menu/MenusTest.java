package menu;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import menu.domain.Menu;
import menu.domain.MenuGroup;
import menu.domain.Menus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenusTest {

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("추천메뉴");
    }

    @DisplayName("고유 사이즈 검증 성공")
    @Test
    void validateDistinctSize() {
        Menu menu1 = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup);
        Menu menu2 = new Menu(2L, "양념+후라이드", BigDecimal.valueOf(19000), menuGroup);
        Menus menus = new Menus(Arrays.asList(menu1, menu2));
        assertThatCode(() -> menus.validateDistinctSize(2))
                .doesNotThrowAnyException();
    }

    @DisplayName("고유 사이즈 검증 실패")
    @Test
    void validateDistinctSizeFail() {
        Menu menu1 = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup);
        Menu menu2 = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup);
        Menus menus = new Menus(Arrays.asList(menu1, menu2));
        assertThatThrownBy(() -> menus.validateDistinctSize(2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

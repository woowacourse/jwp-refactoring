package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("추천메뉴");
    }

    @DisplayName("메뉴 생성 성공")
    @Test
    void create() {
        assertThatCode(() -> new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup))
                .doesNotThrowAnyException();
    }

    @DisplayName("메뉴 생성 실패 - 가격 부재")
    @Test
    void createByNullPrice() {
        assertThatThrownBy(() -> new Menu("후라이드+후라이드", null, menuGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 실패 - 가격 0 미만")
    @Test
    void createByNegativePrice() {
        assertThatThrownBy(() -> new Menu("후라이드+후라이드", BigDecimal.valueOf(-1), menuGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

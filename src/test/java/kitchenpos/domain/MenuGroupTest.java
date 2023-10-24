package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @DisplayName("메뉴에 메뉴 그룹을 등록한다.")
    @Test
    void addMenu() {
        // given
        final MenuGroup menuGroup = new MenuGroup(1L, "메뉴 그룹");
        final Menu menu = new Menu(1L, "메뉴", List.of(new MenuProduct(1L, new Product(1L, "상품", BigDecimal.TEN), 1L)));

        // when
        menuGroup.addMenu(menu);

        // then
        assertThat(menuGroup.getMenus()).contains(menu);
    }

    @DisplayName("메뉴에 메뉴 그룹이 이미 존재하는 경우 예외가 발생한다.")
    @Test
    void addMenu_failExistMenuGroup() {
        // given
        final MenuGroup menuGroup = new MenuGroup(1L, "메뉴 그룹");
        final MenuGroup otherMenuGroup = new MenuGroup(2L, "다른 메뉴 그룹");
        final Menu menu = new Menu(1L, "메뉴", List.of(new MenuProduct(1L, new Product(1L, "상품", BigDecimal.TEN), 1L)));
        otherMenuGroup.addMenu(menu);

        // when
        // then
        assertThatThrownBy(() -> menuGroup.addMenu(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

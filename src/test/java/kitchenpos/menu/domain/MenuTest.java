package kitchenpos.menu.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("객체 생성: 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        final Long id = 1L;
        final String name = "바삭치킨 두마리";
        final BigDecimal price = new BigDecimal(24000);
        final Long menuGroupId = 2L;
        final List<MenuProduct> menuProducts = new ArrayList<>();

        // when
        Menu menu = new Menu(id, name, price, menuGroupId, menuProducts);

        // then
        assertThat(menu.getId()).isEqualTo(1L);
        assertThat(menu.getName()).isEqualTo("바삭치킨 두마리");
        assertThat(menu.getPrice()).isEqualTo(new BigDecimal(24000));
        assertThat(menu.getMenuGroupId()).isEqualTo(2L);
        assertThat(menu.getMenuProducts()).isEmpty();
    }
}
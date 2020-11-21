package kitchenpos.ui;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupRestControllerTest extends ControllerTest {

    @Test
    void create() throws Exception {
        MenuGroup chickenCombo = menuGroup("Chicken Combo");
        assertAll(
                () -> assertThat(chickenCombo.getId()).isEqualTo(1L),
                () -> assertThat(chickenCombo.getName()).isEqualTo("Chicken Combo")
        );
    }

    @Test
    void list() throws Exception {
        // given
        MenuGroup chickenCombo = menuGroup("Chicken Combo");
        MenuGroup pizzaCombo = menuGroup("Pizza Combo");

        // when
        List<MenuGroup> menuGroups = menuGroups();

        // then
        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups.get(0)).isEqualToComparingFieldByField(chickenCombo),
                () -> assertThat(menuGroups.get(1)).isEqualToComparingFieldByField(pizzaCombo)
        );
    }
}
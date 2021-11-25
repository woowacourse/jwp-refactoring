package kitchenpos.menugroup.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupTest {

    @DisplayName("객체 생성 : 객체 정상 생성된다.")
    @Test
    void create() {
        // given
        Long id = 1L;
        String name = "식사류";

        // when
        MenuGroup menuGroup = new MenuGroup(id, name);

        // then
        assertThat(menuGroup.getId()).isEqualTo(id);
        assertThat(menuGroup.getName()).isEqualTo(name);
    }
}

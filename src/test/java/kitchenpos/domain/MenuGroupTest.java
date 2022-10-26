package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupTest {

    @Test
    @DisplayName("아이디를 설정한다")
    void setId() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        Long id = 999L;

        // when
        menuGroup.setId(id);

        // then
        assertThat(menuGroup.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("이름을 설정한다")
    void setName() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        String name = "test";

        // when
        menuGroup.setName(name);

        // then
        assertThat(menuGroup.getName()).isEqualTo(name);
    }
}

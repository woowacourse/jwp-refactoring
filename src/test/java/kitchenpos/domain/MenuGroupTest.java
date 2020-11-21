package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupTest {
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void constructor() {
        MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");

        assertAll(
            () -> assertThat(menuGroup.getId()).isEqualTo(1L),
            () -> assertThat(menuGroup.getName()).isEqualTo("메뉴그룹")
        );
    }
}

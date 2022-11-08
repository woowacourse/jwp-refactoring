package kitchenpos.domain;

import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuGroupTest {

    @DisplayName("이름이 null인 메뉴 그룹을 생성할 수 없다")
    @Test
    void create_nameNull() {
        assertThatThrownBy(() -> new MenuGroup(null));
    }
}

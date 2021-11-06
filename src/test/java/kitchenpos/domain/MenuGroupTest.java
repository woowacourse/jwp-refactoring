package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupTest {

    private static final String MENU_GROUP_NAME = "MENU_GROUP_NAME";

    @DisplayName("MenuGroup 생성 - 실패 - 이름이 null인 경우")
    @Test
    void createFailure() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new MenuGroup(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
